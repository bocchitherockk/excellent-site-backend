package org.green_building.excellent_training.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // this is needed from when we read from the database we need to create an empty object to store the values in it
@Entity(name = "Role")
@Table(name = "roles")
public class Role {

    /********************* id *********************/
    @Id
    @SequenceGenerator (
        name = "roles_sequence",
        sequenceName = "roles_id_sequence",
        allocationSize = 1,
        initialValue = 1
    )
    @GeneratedValue (
        strategy = GenerationType.SEQUENCE,
        generator = "roles_id_sequence"	    
    )
    @Column (
        name = "id",
        unique = true,
        nullable = false,
        insertable = false,
        updatable = false
    )
    private Integer id;

    /********************* name *********************/
    @Column (
        name = "name",
        length = 50, // meaning it is a varchar(50), varchar(255) is the default // it is the same as setting columnDefinition = "varchar(50)"
        nullable = false
    )
    private String name;


    // a constructor that does not have the field 'id' because it is auto generated
    public Role(String name) {
        this.name = name;
    }
}
