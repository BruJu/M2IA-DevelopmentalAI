
# Agent développemental


## Introduction

blabla


Nous avons développé les agents et les environnements en Java. Le code est disponible à l'adresse
https://www.github.com/BruJu/aidev mais pour chaque agent, nous nous efforcerons de décrire l'algorithme
que nous avons implémenté.

## TP 1 : Agent dans un environnement donnant toujours la même réponse à une action donnée



### Principe

```java
class Agent {
    Map<Action, Feedback> interactionApprises; 
    Action actionActuelle;

    Action produireAction() {
        return actionActuelle;
    }
    
    String obtenirFeedback(Action action, Feedback feedbackObtenu) {
        Feedback feedbackAttendu = interactionApprises.get(action);
        
        if (feedbackAttendu == feedbackObtenu) {
            if (actionActuelle_faite_trop_souvent()) {
                changer_d_action();
                return "Ennuyé";
            } else {
                return "Content";
            }
        } else {
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


## TP 2 : 

## TP 3 : 

## TP 4 : 


## Conclusion 

blabla