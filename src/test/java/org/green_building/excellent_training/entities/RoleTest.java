package org.green_building.excellent_training.entities;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.green_building.excellent_training.entities.Role;
import org.green_building.excellent_training.dtos.RoleDto;

public class RoleTest {

    public static int count = 1;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("starting tests");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("ending tests");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("statring test number: " + RoleTest.count);
    }

    @AfterEach
    public void afterEach() {
        System.out.println("ended test number: " + RoleTest.count);
        RoleTest.count++;
    }

    @Test
    public void shouldConvertRoleDtoToRole() {
        // Given
        RoleDto roleDto = new RoleDto(10, "role name");
        // When
        Role role = Role.from(roleDto);
        // Then
        Assertions.assertEquals(roleDto.getId(), role.getId());
        Assertions.assertEquals(roleDto.getName(), role.getName());
    }

    @Test
    public void shouldConvertRoleDtoToNullWhenRoleDtoIsNull() {
        // Given
        RoleDto roleDto = null;
        // When
        Role role = Role.from(roleDto);
        // Then
        Assertions.assertEquals(null, role);
    }

    @Test
    public void shouldConvertRoleDtosToRoles() {
        // Given
        List<RoleDto> roleDtos = new ArrayList<>();
        roleDtos.add(new RoleDto(1, "role 1"));
        roleDtos.add(new RoleDto(2, "role 2"));
        roleDtos.add(new RoleDto(3, "role 3"));
        roleDtos.add(new RoleDto(4, "role 4"));
        roleDtos.add(new RoleDto(5, "role 5"));
        roleDtos.add(new RoleDto(6, "role 6"));
        roleDtos.add(new RoleDto(7, "role 7"));
        roleDtos.add(new RoleDto(8, "role 8"));
        // When
        List<Role> roles = Role.from(roleDtos);
        // Then
        for (int i = 0; i < roleDtos.size(); i++) {
            RoleDto roleDto = roleDtos.get(i);
            Role role = roles.get(i);
            Assertions.assertEquals(roleDto.getId(), role.getId());
            Assertions.assertEquals(roleDto.getName(), role.getName());
        }
    }

    @Test
    public void shouldConvertRoleDtosToListOfNullsWhenRoleDtosAreNull() {
        // Given
        List<RoleDto> roleDtos = new ArrayList<>();
        roleDtos.add(null);
        roleDtos.add(null);
        roleDtos.add(null);
        roleDtos.add(null);
        roleDtos.add(null);
        roleDtos.add(null);
        roleDtos.add(null);
        roleDtos.add(null);
        // When
        List<Role> roles = Role.from(roleDtos);
        // Then
        for (int i = 0; i < roleDtos.size(); i++) {
            Role role = roles.get(i);
            Assertions.assertEquals(null, role);
        }
    }
}
