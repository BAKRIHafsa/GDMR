package com.sqli.gdmr.Enums;

public enum StatusVisite {
    PLANIFIE,
    EN_COURS,
    TERMINE,
    ANNULE,
    VALIDE,// après que le collab a choisit un creneau (validé un créneau parmi d'autres) ou bien le rh a choisi pour lui un creneau
    NON_VALIDE, // si le collab n'est pas d'accord au créneau choisi par le rh
    EN_ATTENTE_CONFIRMATION,
    EN_ATTENTE_VALIDATION, // l'attente de valider un créneau par collab ou chargérh après qui dépasse le délai
    CONFIRME_COLLAB,
    CONFIRME_MED,
}
