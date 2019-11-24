# Agent développemental

**TODO : noms**

## Introduction

**TODO**

Nous avons développé les agents et les environnements en Java. Le code est disponible à l'adresse
https://www.github.com/BruJu/aidev mais pour chaque agent, nous nous efforcerons de décrire l'algorithme
que nous avons implémenté.

## TP 1 : Agent dans un environnement donnant toujours la même réponse à une action donnée

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

| Action | Attendu | Obtenu | Reaction de l'agent |
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

Une fois que la  map a été initialisée pour l'action, l'agent n'a aucun problème pour prédire l'action obtenue et se
lasse vite.

Nous avons également implémenté l'environnement qui renvoie 2 à l'action 1 et 1 à l'action 2. Nous ne mettons pas la
trace car elle est similaire à la précédente : après la première fois que l'agent fait une action donnée, il prédit à
chaque fois les réactions suivantes provenant de cette action se lasse rapidement.

Nous remarquons que lors de la phase d'apprentissage, l'agent effectue 4 fois de suite la même action (une fois pour
apprendre, la réponse le surprend, puis 3 fois où il obtient la réponse attendue). Lorsqu'il revient à une action, il
n'effectue plus que 3 fois l'action.

Sur un agent plus évolué, on pourrait imaginer implémenter volontairement ce mécanisme en faisant se lasser de plus
en plus vite sur les intéractions (ou les séquences d'intéractions) qu'il connait déjà pour qu'il explore de nouvelles
possibilités et sorte de sa zone de confort.

## TP 2 : Agent préférant certaines paires d'action - feedback

### Implémentation

Le fonctionnement de l'agent est identique au premier, excepté pour la fonction de changement d'action.
Si dans le TP1, la fonction de changement d'action est triviale (renvoie 1 si l'action faite était la 2, renvoie 2 si
l'action faite était la 1), ici nous avons deux phases dans la fonction de changement d'état : une phase d'exploration
et une phase d'exploitation.

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
        // dont on vinet de se lasser
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
Nous considérons que la valeur d'une action est égale à la valeur de la paire (action, feedback) qu'on a obtenu la
dernière fois qu'on a fait cette action.

### Trace


| Action | Attendu | Obtenu | Reaction de l'agent | Valence |
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

Par abus, nous avons gardé le fait qu'un agent est content même si la valeur qu'il a de l'interaction est négative : il
est "content" d'avoir prédit correctement l'intéraction.

Lors de la phase d'exploration, l'agent explore toutes les actions.
Lors de la phase d'exploitation, il n'effectue pas l'action 2. Il préfère alterner entre 1 et 3 qui ont une valeur plus
grande que 2 (dans son système de valeur).


## TP 3 : Environnement dont la réponse change selon l'action précédente

### Implémentation

Nous décorons la map d'intéractions apprises (qui constitue les suffixes) par une autre map d'intéractions
précédentes (qui liste les préfixes).

Chaque intéraction préfixe possible donne ainsi à l'agent une liste d'intéractions suffixes attendues.

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

Si pour une intéraction précédente et une action, nous n'avons pas de feedback, alors nous considérons que
la valeur de l'intéraction est 0 (neutre) pour la favoriser par rapport à une interaction qui serait défavorable.

Cette approche prudente ne permet néanmoins pas d'explorer toutes les possibilités : si la séquence AaFbAc produit
un feedback Fd et que AcFd a pour valeur 1, si on l'explore avant AaFbAeFg et que AeFg a pour valeur 2, alors AeFg ne
sera jamais exploré alors qu'il est plus favorable.

Nous avons retiré la mécanique d'ennui afin de ne pas complexifier inutilement le code : en effet
l'environnement que nous essayons de faire apprendre à l'agent et son système de valeur font qu'il ne
devrait pas répéter son action (parce qu'il n'aime pas avoir un feedback de 1 qui serait
issus du fait qu'il s'arréterait de changer d'action).


### Trace 


| Action | Attendu | Obtenu | Reaction de l'agent | Valence | Séquence apprise |
| ------ | ------- | --------------- | ------------------- | --- | --- |
| 1 | 0 | 2 | Surprised | 1 | N/A |
| 1 | 0 | 1 | Learned | -1 | [I12;I11] |
| 1 | 0 | 1 | Learned | -1 | [I11;I11] |
| 2 | 0 | 2 | Learned | 1 | [I11;I22] |
| 1 | 0 | 2 | Learned | 1 | [I22;I12] |
| 2 | 0 | 2 | Learned | 1 | [I12;I22] |
| 1 | 2 | 2 | Happy | 1 |  |
| 2 | 2 | 2 | Happy | 1 |  |
| 1 | 2 | 2 | Happy | 1 |  |
| 2 | 2 | 2 | Happy | 1 |  |


On remarque que le résultat de la première intéraction n'est utilisé que pour construire des
séquences (il n'apprend rien au pas 1), tandis que les autres sont utilisés à la fois pour construire la séquence
suivante (comme préfixe) et pour pouvoir finir la séquence en cours (comme suffixe).


## TP 4 : Apprentissage d'environnements pouvant changer

### Implémentation

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

### Vue globale

Nous avons exécuté sur les 4 environnements demandés (environnement envoyant toujours 1, environnement envoyant toujours
2, environnement envoyant 2 si l'action est différente de la précédente et environnement se comportant comme le 3 puis
comme le 1) avec les 3 systèmes de valeur demandés (un agent qui
préfère les intéractions issues de l'action 1, un agent qui préfère les
intéractions issues de l'action 2 et un agent préférent obtenir le feedback 2).

Nous représentons dans le tableau le bilan de l'exécution de ces agents sous le format a - b - c où a est le
nombre d'intéractions positives lors des 10 premiers pas, b est le nombre d'intéractions
positives pendant les 10 pas suivant et c est le nombre
d'intéractions positives lors des intéractions de 100 à 199.

| Favorise | Action 1 | Action 2 | Feedback 2 |
| --- | --- | --- | --- |
| *Env1* | 10 - 10 - 100 | 7 - 10 - 100 | 7 - 10 - 100 |
| *Env2* | 10 - 10 - 100 | 7 - 10 - 100 | 10 - 10 - 100 |
| *Env3* | 10 - 10 - 100 | 5 - 10 - 100 | 8 - 10 - 100 |
| *Env4* | 10 - 10 - 100 | 7 - 8 - 100 | 7 - 6 - 100 |

- L'agent ayant le premier système de valeur obtient toujours des intéractions positives :
lorsqu'il apprend il effectue par défaut la première action. Lorsqu'il a une connaissance sur l'environnement, il
a retenu que l'action 1 était positive donc il continue à l'exploiter. Il ne se rend pas
compte que l'environnement a changé.
    - On peut dire que cet agent est totalement passif par rapport à l'environnement.

- L'agent favorisant les intéractions issues de l'action 2 teste naïvement l'action 1 en premier
lorsqu'il ne sait pas quoi faire. Il n'apprend rien de sa première action. Par la suite, à chaque
fois qu'il va faire une nouvelle intéraction, l'agent va essayer l'action 1 pour ensuite
se rabattre sur l'action 2 lors des prochaines occurences. Ainsi, on peut faire
une corrélation entre le nombre d'échecs et le nombre de séquences qu'il a appris.
    - On note que contrairement à l'agent 1, l'agent 2 peut avoir une intuition que l'environnement
a changé.
    - On peut conclure que cet agent, si il ne cherche pas à manipuler l'environnement, a la curiosité d'étudier
    rapidement ce que l'action 1 (qu'il déteste) peut produire, sans chercher à remettre
    en cause ce qu'il a ensuite appris sur l'action 1.

- L'agent favorisant le feedback 2, contrairement aux deux précédents, essaye de produire un résultat donné
de la part de l'environnement.

- Dans tous les cas sur le long terme, tous nos agents finissent par savoir comment exploiter l'environnement pour avoir
des intéractions qu'il aime.

### Etude d'un agent cherchant le feedback 2 dans un environnement changeant

**TODO**


## Conclusion 

**TODO**
