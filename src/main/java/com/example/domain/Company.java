package com.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private Set<Company> subordinates = new HashSet<>();

    @ManyToOne
    private Company parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Company> getSubordinates() {
        return subordinates;
    }

    public Company subordinates(Set<Company> companies) {
        this.subordinates = companies;
        return this;
    }

    public Company addSubordinate(Company company) {
        subordinates.add(company);
        company.setParent(this);
        return this;
    }

    public Company removeSubordinate(Company company) {
        subordinates.remove(company);
        company.setParent(null);
        return this;
    }

    public void setSubordinates(Set<Company> companies) {
        this.subordinates = companies;
    }

    public Company getParent() {
        return parent;
    }

    public Company parent(Company company) {
        this.parent = company;
        return this;
    }

    public void setParent(Company company) {
        this.parent = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        if(company.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Company{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
