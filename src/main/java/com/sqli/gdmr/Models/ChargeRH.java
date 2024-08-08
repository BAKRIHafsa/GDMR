package com.sqli.gdmr.Models;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CHARGERH")
@Data
public class ChargeRH extends User {
//    @Id
//    @GeneratedValue
//    private Long IdChargéRH;
    @OneToMany(mappedBy = "chargeRH", fetch = FetchType.LAZY)
    private List<Creneau> creneaus =new ArrayList<>();
    private String departement;

}
