package org.boris.api1014.member.domain;

public enum MemberRole {

    USER("USER"),ADMIN("ADMIN");

    String role;

    MemberRole(String role)
    {
        this.role = role;
    }
}
