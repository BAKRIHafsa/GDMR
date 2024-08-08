package com.sqli.gdmr.Mappers;

import com.sqli.gdmr.DTOs.ModifieCollaborateur;
import com.sqli.gdmr.Models.ChargeRH;
import com.sqli.gdmr.Models.User;
import com.sqli.gdmr.Models.Collaborateur;

public class ModifieCollaborateurMapper {

    public static void updateUserFromDto(User user, ModifieCollaborateur dto) {
        if (dto == null) return;

        if (user instanceof Collaborateur) {
            Collaborateur collaborateur = (Collaborateur) user;
            collaborateur.setRole(dto.getRole());
            collaborateur.setDepartement(dto.getDepartement()); // Handle Collaborateur's departement
        }
        else if (user instanceof ChargeRH) {
            ChargeRH chargeRH = (ChargeRH) user;
            chargeRH.setRole(dto.getRole());
            chargeRH.setDepartement(dto.getDepartement()); // Handle ChargeRH's departement
        }
    }
}
