package com.sqli.gdmr.Enums;

public enum StatusVisite {
    PLANIFIE,
    EN_COURS,
    TERMINE,
    ANNULE,
    VALIDE,// après que le collab a choisit un creneau
    NON_VALIDE, // si le collab n'est pas d'accord au créneau choisi par le rh

    EN_ATTENTE_CREATION_CRENEAU,
    EN_ATTENTE_VALIDATION, // l'attente de valider un créneau par collab
//    CONFIRME_COLLAB,
//    CONFIRME_MED,
}
