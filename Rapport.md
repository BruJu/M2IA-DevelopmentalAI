
<h1 style="text-align:center;">Agent développemental</h1>

*Étudiant 1* : Julian BRUYAT 11706770

*Étudiant 2* : Jean-Philippe TISSERAND 11926733

## Introduction

Les agents développementaux sont des agents qui n'ont aucune connaissance à priori de l'environnement.
Leur particularité est qu'ils apprennent en étant "actifs" : contrairement à un agent classique qui apprendrait en observant l'environnement puis en faisant une action, les agents développementaux se développent en  faisant une action et en observant quelle réponse leur donne l'environnement.

Ce TP a pour objectif de développer des agents développementaux simples évoluant dans des environnements simples sans en avoir de connaissance à priori.

Nous avons développé les agents et les environnements en Java. Le code est disponible à l'adresse https://www.github.com/BruJu/aidev mais pour chaque agent, nous nous efforcerons de décrire l'algorithme que nous avons implémenté en utilisant un pseudo code Java donnant l'implémentation générale (qui ne gère  pas les cas particuliers comme les valeurs `null` qui relèvent plus du détail d'implémentation).

## TP 1 : Agent dans un environnement donnant toujours la même réponse à une action donnée

Le premier TP consiste à développer un agent qui apprend les réactions produites par un environnement simple.

L'environnement est simple dans le sens où il renvoie toujours la même réaction à une action donnée. L'objectif de l'agent est d'apprendre toutes les interactions disponibles dans cet environnement.

### Implémentation

```java
class Agent {
    Map<Action, Feedback> interactionsApprises; 
    Action actionActuelle;

    Action produireAction() {
        return actionActuelle;
    }
    
    String obtenirFeedback(Action action, Feedback feedbackObtenu) {
        // On récupère le feedback qu'on attendait de l'action qu'on a fait
        Feedback feedbackAttendu = interactionsApprises.get(action);
        
        if (feedbackAttendu == feedbackObtenu) {
            // On a eu la réponse qu'on attendait : risque d'ennui
            if (actionActuelle_faite_trop_souvent()) {
                changer_d_action(); // Inverse l'action actuelle entre 1 et 2
                return "Ennuyé";
            } else {
                return "Content";
            }
        } else {
            // On apprend la paire Action effectuée - feedback reçu
            interactionApprises.put(action, feedbackObtenu);
            return "Surpris";
        }
    }
}
```

### Résultats sur l'environnement 1

L'environnement 1 est un environnement qui produit un feedback égal à l'action faite.

| Action | Attendu | Obtenu | Réaction de l'agent |
| ------ | ------- | --------------- | ------------------- |
| 1 | 0 | 1 | Surprised |
| 1 | 1 | 1 | Happy |
| 1 | 1 | 1 | Happy |
| 1 | 1 | 1 | Bored |
| 2 | 0 | 2 | Surprised |
| 2 | 2 | 2 | Happy |
| 2 | 2 | 2 | Happy |
| 2 | 2 | 2 | Bored |
| 1 | 1 | 1 | Happy |
| 1 | 1 | 1 | Happy |
| 1 | 1 | 1 | Bored |
| 2 | 2 | 2 | Happy |
| 2 | 2 | 2 | Happy |
| 2 | 2 | 2 | Bored |
| 1 | 1 | 1 | Happy | 

Une fois que la map a été initialisée pour l'action, l'agent n'a aucun problème pour prédire l'action obtenue et se
lasse vite.

Nous avons également implémenté l'environnement qui renvoie 2 à l'action 1 et 1 à l'action 2. Nous ne mettons pas la trace car elle est similaire à la précédente : après la première fois que l'agent fait une action donnée, il prédit à chaque fois correctement le feedback provenant de cette action et se lasse rapidement.

Nous remarquons que lors de la phase d'apprentissage, l'agent effectue 4 fois de suite la même action (une fois pour apprendre, la réponse le surprend, puis 3 fois où il obtient la réponse attendue). Lorsqu'il revient à une action, il n'effectue plus que 3 fois l'action.

Sur un agent plus évolué, on pourrait imaginer implémenter volontairement ce mécanisme en faisant se lasser de plus en plus vite sur les interactions (ou les séquences d'interactions) qu'il connait déjà pour qu'il explore de nouvelles possibilités et sorte de sa zone de confort.

## TP 2 : Agent préférant certaines paires d'action - feedback

### Implémentation

Le fonctionnement de l'agent est identique au premier, excepté pour la fonction de changement d'action.
Si dans le TP1, la fonction de changement d'action est triviale (renvoie 1 si l'action faite était la 2, renvoie 2 si l'action faite était la 1), ici nous avons deux phases dans la fonction de changement d'état : une phase d'exploration et une phase d'exploitation.

```java
class Agent {
    /* Code de l'agent précédent */
    
    Map<Pair<Action, FeedBack>, Integer> valeurs;
    
    void changer_d_action() {
        // Exploration
        for (Action action : actionPossibles) {
            // On explore toutes les actions possibles.
            // Si il y en a une que l'on n'a pas appris, on l'explore
            if (!interactionsApprises.contains(action)) {
                actionActuelle = action;
                return;
            }
        }
        
        // Exploitation
        // On cherche la meilleure action possible qui est différente de celle
        // dont on vient de se lasser
        meilleure_action_possible = Aucune;
        for (Action action : actionPossibles) {
            if (action != actionActuelle) {
                if (valeur_action(action) > valeur_action(meilleure_action_possible))
                    meilleure_action_possible = action;
            }
        }
        
        actionActuelle = meilleure_action_possible;
    }
    
    int valeur_action(Action action) {
        return valeurs.get(Paire(action, interactionsApprises.get(action)));
    }
}
```
Nous considérons que la valeur d'une action est égale à la valeur de la paire (action, feedback) qu'on a obtenue la dernière fois que l'on a fait cette action.

### Trace

| Action | Attendu | Obtenu | Réaction de l'agent | Valence |
| ------ | ------- | --------------- | ------------------- | --- |
| 1 | 0 | 1 | Surprised | 1 |
| 1 | 1 | 1 | Happy | 1 |
| 1 | 1 | 1 | Happy | 1 |
| 1 | 1 | 1 | Bored | 1 |
| 2 | 0 | 2 | Surprised | -1 |
| 2 | 2 | 2 | Happy | -1 |
| 2 | 2 | 2 | Happy | -1 |
| 2 | 2 | 2 | Bored | -1 |
| 3 | 0 | 2 | Surprised | 1 |
| 3 | 2 | 2 | Happy | 1 |
| 3 | 2 | 2 | Happy | 1 |
| 3 | 2 | 2 | Bored | 1 |
| 1 | 1 | 1 | Happy | 1 |
| 1 | 1 | 1 | Happy | 1 |
| 1 | 1 | 1 | Bored | 1 |
| 3 | 2 | 2 | Happy | 1 |
| 3 | 2 | 2 | Happy | 1 |
| 3 | 2 | 2 | Bored | 1 |
| 1 | 1 | 1 | Happy | 1 |
| 1 | 1 | 1 | Happy | 1 |
| 1 | 1 | 1 | Bored | 1 |

Par abus, nous avons gardé le fait qu'un agent est content même si la valeur qu'il donne à l'interaction est négative : il est "content" d'avoir prédit correctement l'interaction.

Lors de la phase d'exploration, l'agent explore toutes les actions.
Lors de la phase d'exploitation, il n'effectue pas l'action 2. Il préfère alterner entre l'action 1 et 3 qui ont une valeur plus
grande que la 2 (dans son système de valeur).


## TP 3 : Environnement dont la réponse change selon l'action précédente

### Implémentation

Nous décorons la map d'interactions apprises (qui constitue les suffixes) par une autre map d'interactions
précédentes (qui liste les préfixes).

Chaque interaction préfixe possible donne ainsi à l'agent une liste d'interactions suffixes attendues.

```java
class Agent {
    Map<Pair<Action, Feedback>, Map<Action, Feedback>> sequencesApprises;
    Pair<Action, Feedback> interactionPrecedente;

    Action produireAction() {
        Map<Action, Feedback> interactionsAprises = sequencesApprises.get(interactionPrecedente);
        return choisir_laction_dont_la_valeur_de_linteraction_est_la_plus_grande(interactionsAprises);
    }
    
    Action choisir_laction_dont_la_valeur_de_linteraction_est_la_plus_grande(Map<Action, feedback> map) {
        Action actionChoisie = Aucune;
        int feedbackActionChoisie = -Infini;
        
        for (Action action : actionPossibles) {
            int feedback = map.getOrDefault(action, 0);
            if (valeur(action, feedback) > valeur(actionChoisie, feedbackActionChoisie)) {
                actionChoisie = action;
                feedbackActionChoisie = feedback;
            }
        }
        
        return actionChoisie;
    }
    
    String obtenirFeedback(Action action, Feedback feedbackObtenu) {
        if (sequencesApprises.get(interactionPrecedente).get(action) == feedbackObtenu) {
            interactionPrecedente = Pair(action, feedbackObtenu);
            return "Content";
        } else {
            sequencesApprises.get(interactionPrecedente).put(action, feedbackObtenu);
            interactionPrecedente = Pair(action, feedbackObtenu);
            return "A appris";
        }
    }
}
```

Si pour un couple "interaction précédente + action", nous n'avons pas de feedback, alors nous considérons que
la valeur de l'interaction est 0 (neutre) pour la favoriser par rapport à une interaction qui serait défavorable.

Cette approche prudente ne permet néanmoins pas d'explorer toutes les possibilités : si la séquence AaFbAc produit un feedback Fd et que AcFd a pour valeur 1, si on l'explore avant AaFbAeFg et que AeFg a pour valeur 2, alors AeFg ne sera jamais exploré alors qu'elle est plus favorable.

Nous avons retiré la mécanique d'ennui afin de ne pas complexifier inutilement le code : en effet
l'environnement que nous essayons de faire apprendre à l'agent et son système de valeur font qu'il ne
devrait pas répéter son action (parce qu'il n'aime pas avoir un feedback de 1 qui serait
issus du fait qu'il s'arrêterait de changer d'action).


### Trace 


| Étape | Action | Attendu | Obtenu | Réaction de l'agent | Valence | Séquence apprise |
| ----- | ------ | ------- | --------------- | ------------------- | --- | --- |
| #1 | 1 | 0 | 2 | Surprised | 1 | N/A |
| #2 | 1 | 0 | 1 | Learned | -1 | [I12;I11] |
| #3 | 1 | 0 | 1 | Learned | -1 | [I11;I11] |
| #4 | 2 | 0 | 2 | Learned | 1 | [I11;I22] |
| #5 | 1 | 0 | 2 | Learned | 1 | [I22;I12] |
| #6 | 2 | 0 | 2 | Learned | 1 | [I12;I22] |
| #7 | 1 | 2 | 2 | Happy | 1 |  |
| #8 | 2 | 2 | 2 | Happy | 1 |  |
| #9 | 1 | 2 | 2 | Happy | 1 |  |
| 10 | 2 | 2 | 2 | Happy | 1 |  |

On remarque que le résultat de la première interaction n'est utilisé que pour construire des
séquences (il n'apprend rien au pas 1), tandis que les autres sont utilisés à la fois pour construire la séquence
suivante (comme préfixe) et pour pouvoir finir la séquence en cours (comme suffixe).

On peut interpréter le comportement de cet agent comme étant un agent classique en IA : au début il cherche
à observer son environnement mais il est obligé de déclencher des actions pour observer (étapes #1 à #6). Une fois
à l'étape 7, l'agent observe qu'il vient de produire l'interaction I22, et qu'il connait déjà une chaîne qui lui
plait avec pour prefixe I22 (la chaîne I22 - I12 produite à l'étape #5). Il l'exploite donc et envoie l'action 1
à l'environnement.


## TP 4 : Apprentissage d'environnements pouvant changer

Le but de ce TP est de voir si nous arrivons maintenant à générer des comportement intelligents avec nos agents dans des environements différents. 

### Implémentation

```java
class Agent {
    Map<Pair<Action, Feedback>, Map<Action, RapportAvecFeedback>> sequencesApprises;
    Pair<Action, Feedback> interactionPrecedente;

    Action produireAction() {
        Map<Action, Feedback> interactionsAprises = sequencesApprises.get(interactionPrecedente);
        return choisir_laction_dont_la_valeur_de_linteraction_est_la_plus_grande(interactionsAprises);
    }
    
    Action choisir_laction_dont_la_valeur_de_linteraction_est_la_plus_grande(Map<Action, RapportAvecFeedback> map) {
        Action actionChoisie = Aucune;
        
        for (Action action : actionPossibles) {
            RapportAvecFeedback rapport = map.get(action);
            if (valeur(action, rapport) > valeur(actionChoisie, rapport)) {
                actionChoisie = action;
            }
        }
        
        return actionChoisie;
    }
    
    int valeur(int action, RapportAvecFeedback rapport) {
        int s = 0;
        for (Feedback feedback : feedbackPossible) {
            s = s + rapport.get(feedback).poids * valeurs.get(action, feedback);
        }
        return s;
    }
    
    String obtenirFeedback(Action action, Feedback feedbackObtenu) {
        int feedbackMajoritaire = sequencesApprises.get(interactionPrecedente).get(action).getFeedbackMajoritaire();
        
        sequencesApprises.get(interactionPrecedente).get(action).register(feedbackObtenu);
        interactionPrecedente = Pair(action, feedbackObtenu);
        
        if (obtenirFeedback == feedbackObtenu) {
            return "Content";
        } else {
            return "Surpris";
        }
    }
}
```

Les principales modifications sont :
- Une action n'est plus évaluée selon le feedback auquel on s'attend (qui est le dernier qu'on a eu pour l'interaction précédente et action faite à la suite de cette interaction) mais l'ensemble des feedback déjà obtenus.
- A chaque pas, on enregistre qu'on a vu la séquence interaction précédente - interaction actuelle.

### Vue globale

Nous avons exécuté sur les 4 environnements demandés (environnement envoyant toujours 1, environnement envoyant toujours
2, environnement envoyant 2 si l'action est différente de la précédente et environnement se comportant comme le 3 puis
comme le 1) avec les 3 systèmes de valeur demandés (un agent qui
préfère les interactions issues de l'action 1, un agent qui préfère les
interactions issues de l'action 2 et un agent préfèrent obtenir le feedback 2).

Nous représentons dans le tableau le bilan de l'exécution de ces agents sous le format a - b - c où a est le
nombre d'interactions positives lors des 10 premiers pas, b est le nombre d'interactions
positives pendant les 10 pas suivant et c est le nombre
d'interactions positives lors des interactions de 100 à 199. Pour la validité de l'agent, il faut
que le troisième nombre soit égal à 100 (sinon c'est qu'il n'a pas bien appris).

| Favorise | Action 1 | Action 2 | Feedback 2 |
| --- | --- | --- | --- |
| *Env1* | 10 - 10 - 100 | 7 - 10 - 100 | 7 - 10 - 100 |
| *Env2* | 10 - 10 - 100 | 7 - 10 - 100 | 10 - 10 - 100 |
| *Env3* | 10 - 10 - 100 | 5 - 10 - 100 | 8 - 10 - 100 |
| *Env4* | 10 - 10 - 100 | 7 - 8 - 100 | 7 - 6 - 100 |

- L'agent ayant le premier système de valeur obtient toujours des interactions positives :
lorsqu'il apprend il effectue par défaut la première action. Lorsqu'il a une connaissance sur l'environnement, il
a retenu que l'action 1 était positive donc il continue à l'exploiter. Il ne se rend pas
compte que l'environnement a changé.
    - On peut dire que cet agent est totalement passif par rapport à l'environnement.

- L'agent favorisant les interactions issues de l'action 2 teste naïvement l'action 1 en premier
lorsqu'il ne sait pas quoi faire. Il n'apprend rien de sa première action. Par la suite, à chaque
fois qu'il va faire une nouvelle interaction, l'agent va essayer l'action 1 pour ensuite
se rabattre sur l'action 2 lors des prochaines occurrences. Ainsi, on peut faire
une corrélation entre le nombre d'échecs et le nombre de séquences qu'il a appris.
    - On note que contrairement à l'agent 1, l'agent 2 peut avoir une intuition que l'environnement
a changé.
    - On peut conclure que cet agent, si il ne cherche pas à manipuler l'environnement, a la curiosité d'étudier
    rapidement ce que l'action 1 (qu'il déteste) peut produire, sans chercher à remettre
    en cause ce qu'il a ensuite appris sur l'action 1.

- L'agent favorisant le feedback 2, contrairement aux deux précédents, essaye de produire un résultat donné
de la part de l'environnement. Dans les environnements 1, 2 et 3 de cette expérience, il fonctionne comme le
ferait l'agent du TP3 (on voit que à part lors de la phase d'initialisation, il a 100% de réussite).

- Dans tous les cas sur le long terme, tous nos agents finissent par savoir comment exploiter l'environnement pour avoir
des interactions qu'ils aiment.

### Étude d'un agent cherchant le feedback 2 dans un environnement changeant

Le tableau précédent montre que l'agent est capable de s'adapter à l'environnement 4.

Nous allons étudier la trace pour un agent qui attend le feedback 2.

| Étape | Action | Attendu | Obtenu | Réaction de l'agent | Valence | Préfixe | Évaluation Action 1 | Évaluation Action 2 |
| ----- | ------ | ------- | ------ | ------------------- | ------- | ------- | --------------------- | --------------------- |
| #1 | 1 | 0 | 1 | Surprised | -1 | N/A | N/A | N/A |
| #2 | 1 | 1 | 1 | Happy | -1 | I11 | 0+0=0 | 0+0=0 |
| #3 | 2 | 1 | 2 | Surprised | 1 | I11 | -1+0=-1 | 0+**0**=0 |
| #4 | 1 | 1 | 1 | Happy | -1 | I22 | 0+0=0 | 0+0=0 |
| #5 | 2 | 2 | 2 | Happy | 1 | I11 | -1+0=-1 | 0+1=1 |
| #6 | 2 | 1 | 2 | Surprised | 1 | I22 | -1+0=-1 | 0+**0**=0 |
| #7 | 2 | 2 | 2 | Happy | 1 | I22 | -1+0=-1 | 0+1=1 |
| #8 | 2 | 2 | 2 | Happy | 1 | I22 | -1+0=-1 | 0+2=2 |
| #9 | 2 | 2 | 2 | Happy | 1 | I22 | -1+0=-1 | 0+3=3 |
| #10 | 2 | 2 | 2 | Happy | 1 | I22 | -1+0=-1 | 0+4=4 |
| **#11** | 2 | 2 | 2 | Happy | 1 | I22 | -1+0=-1 | 0+5=5 |
| #12 | 2 | 2 | 1 | Surprised | -1 | I22 | -1+0=-1 | **0**+6=6 |
| #13 | 1 | 1 | 2 | Surprised | 1 | I21 | 0+**0**=0 | 0+0=0 |
| #14 | 1 | 1 | 1 | Happy | -1 | I12 | 0+0=0 | 0+0=0 |
| #15 | 2 | 2 | 2 | Happy | 1 | I11 | -1+0=-1 | 0+2=2 |
| #16 | 2 | 2 | 1 | Surprised | -1 | I22 | -1+0=-1 | -**1**+6=5 |
| #17 | 1 | 2 | 2 | Happy | 1 | I21 | 0+1=1 | 0+0=0 |
| #18 | 2 | 1 | 2 | Surprised | 1 | I12 | -1+0=-1 | 0+**0**=0 |
| #19 | 2 | 2 | 1 | Surprised | -1 | I22 | -1+0=-1 | -**2**+6=4 |
| #20 | 1 | 2 | 2 | Happy | 1 | I21 | 0+2=2 | 0+0=0 |
| #21 | 2 | 2 | 2 | Happy | 1 | I12 | -1+0=-1 | 0+1=1 |
| #22 | 2 | 2 | 1 | Surprised | -1 | I22 | -1+0=-1 | -**3**+6=3 |
| #23 | 1 | 2 | 2 | Happy | 1 | I21 | 0+3=3 | 0+0=0 |
| #24 | 2 | 2 | 2 | Happy | 1 | I12 | -1+0=-1 | 0+2=2 |
| #25 | 2 | 2 | 1 | Surprised | -1 | I22 | -1+0=-1 | -**4**+6=2 |
| #26 | 1 | 2 | 2 | Happy | 1 | I21 | 0+4=4 | 0+0=0 |
| #27 | 2 | 2 | 2 | Happy | 1 | I12 | -1+0=-1 | 0+3=3 |
| #28 | 2 | 2 | 1 | Surprised | -1 | I22 | -1+0=-1 | -**5**+6=1 |
| #29 | 1 | 2 | 2 | Happy | 1 | I21 | 0+5=5 | 0+0=0 |
| #30 | 2 | 2 | 2 | Happy | 1 | I12 | -1+0=-1 | 0+4=4 |
| #31 | 2 | 1 | 1 | Happy | -1 | I22 | -1+0=-1 | -6+6=0 |
| #32 | 1 | 2 | 2 | Happy | 1 | I21 | 0+6=6 | 0+0=0 |
| #33 | 2 | 2 | 2 | Happy | 1 | I12 | -1+0=-1 | 0+5=5 |
| #34 | 1 | 1 | 2 | Surprised | 1 | I22 | -1+**0**=-1 | -7+6=-1 |
| #35 | 2 | 2 | 2 | Happy | 1 | I12 | -1+0=-1 | 0+6=6 |
| #36 | 1 | 1 | 2 | Surprised | 1 | I22 | -1+**1**=0 | -7+6=-1 |
| #37 | 2 | 2 | 2 | Happy | 1 | I12 | -1+0=-1 | 0+7=7 |
| #38 | 1 | 2 | 2 | Happy | 1 | I22 | -1+2=1 | -7+6=-1 |
| #39 | 2 | 2 | 2 | Happy | 1 | I12 | -1+0=-1 | 0+8=8 |
| #40 | 1 | 2 | 2 | Happy | 1 | I22 | -1+3=2 | -7+6=-1 |

Note : L'environnement change entre l'étape 10 et l'étape 11.

Nous représentons dans les colonnes "évaluation action" l'arbitrage qu'a fait l'agent
avant d'effectuer l'action. Lorsqu'il se trompe de feedback qu'il reçoit (il attendait
le feedback 1 mais il a obtenu le 2), nous représentons en gras le numéro correspondant à ce
feedback.

Le principal problème que rencontre l'agent est le fait qu'il a appris la séquence I22-I22 et
l'a rencontré 6 fois. Dans la mesure où celle-ci n'est plus valide dans l'environnement 3, il
doit se confronter 7 fois à la séquence I22-I21 pour "désapprendre" I22-I22 et considérer
que désormais, la valeur produite par I21 dépasse la valeur produite par I22 (-7 x 6 = -1) et
recommencer à explorer I22-I1x dont la valeur est égale à I22-I2x lors de l'évaluation de l'étape 34,
puis la dépasse après l'étape 36.

Après l'étape 36, l'agent réussit à n'avoir que des interactions positives : il est
suffisamment habitué au nouvel environnement et déleste ses anciennes habitudes (les poids liés à l'ancien environnement sont moins élevés que ceux liés au nouveau).

Le modèle de l'agent de ce TP converge cependant moins vite que celui du TP 3 sur ce
même environnement. Néanmoins il pourrait s'adapter à des valences différentes de -1 et 1. En particulier,
dans un environnement qui renverrait le feedback 1 une fois sur 10 et le feedback 2 sinon, si
l'agent porte une valeur de 50 au feedback 1 et -1 au feedback 2, ce modèle peut potentiellement
favoriser l'action menant à ces feedback, en étant conscient qu'il aura surement le feedback 2 mais qu'il
peut gagner beaucoup, alors que l'agent du TP 3 restera sur l'idée que cette interaction produit un rendement négatif. 

## Conclusion 

Ce TP nous a montré que sans connaissance des environnements, on peut construire des agents
qui s'adaptent à des environnements divers. Les agents des TP 3 et 4 en particulier le montrent en
utilisant une représentation interne totalement différente de la réalité des environnements
dans lesquels ils évoluent : une représentation sous forme de chaine de deux interactions
fréquentes, alors que les environnements peuvent répondre à des règles légèrement plus complexes,
comme simuler un autre environnement.  

De plus, nous pouvons à partir d'une implémentation déterministe et concrète déduire
du comportement de l'agent des comportements plus abstraits, comme voir le comportement de l'agent
du TP 4 avec le second système de valeur comme étant un agent ayant une forme de curiosité mineure,
alors que nous n'avons pas chercher une seule seconde à implémenter ce comportement dans l'agent. Les comportements émergent des agents et de leurs interactions avec l'environnement.
