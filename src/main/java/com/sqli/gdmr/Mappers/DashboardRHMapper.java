package com.sqli.gdmr.Mappers;

import com.sqli.gdmr.DTOs.DashboardRHDTO;
import com.sqli.gdmr.Models.Creneau;

public class DashboardRHMapper {
    public static DashboardRHDTO toDashboardRHDTO(Creneau creneau) {
        if (creneau == null) {
            return null;
        }

        DashboardRHDTO dto = new DashboardRHDTO();
        dto.setStatusVisite(creneau.getStatusVisite());
        dto.setTypeVisite(creneau.getTypeVisite());
        dto.setHeureDebutVisite(creneau.getHeureDebutVisite());
        dto.setHeureFinVisite(creneau.getHeureFinVisite());
        dto.setDate(creneau.getDate());

        if (creneau.getCollaborateur() != null) {
            dto.setNomCollaborateur(creneau.getCollaborateur().getNom());
            dto.setPrenomCollaborateur(creneau.getCollaborateur().getPrenom());
        }

        if (creneau.getMedecin() != null) {
            dto.setNomMedecin(creneau.getMedecin().getNom());
            dto.setPrenomMedecin(creneau.getMedecin().getPrenom());
        }

        return dto;
    }

    public static Creneau toCreneau(DashboardRHDTO dto) {
        if (dto == null) {
            return null;
        }

        Creneau creneau = new Creneau();
        creneau.setStatusVisite(dto.getStatusVisite());
        creneau.setTypeVisite(dto.getTypeVisite());
        creneau.setHeureDebutVisite(dto.getHeureDebutVisite());
        creneau.setHeureFinVisite(dto.getHeureFinVisite());
        creneau.setDate(dto.getDate());

        return creneau;
    }
}
