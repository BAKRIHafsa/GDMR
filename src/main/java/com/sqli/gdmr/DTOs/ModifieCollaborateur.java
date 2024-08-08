package com.sqli.gdmr.DTOs;

import com.sqli.gdmr.Enums.Role;
import lombok.Data;

@Data
public class ModifieCollaborateur {
    private Role role;
    private String departement;
}
