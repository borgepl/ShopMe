package com.deborger.shopme.common.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(length = 128, nullable = false, unique = true)
    private String name;

    @Column(length = 64, nullable = false, unique = true)
    private String alias;

    @Column(length = 128, nullable = false)
    private String image;

    private Boolean enabled;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children = new HashSet<>();

    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
    }

    public static Category CopyIdAndName(Category category) {
        Category categoryCopy = new Category();
        categoryCopy.setId(category.getId());
        categoryCopy.setName(category.getName());
        return categoryCopy;
    }

    public static Category CopyIdAndName(Integer id, String name) {
        Category categoryCopy = new Category();
        categoryCopy.setId(id);
        categoryCopy.setName(name);
        return categoryCopy;
    }

    public Category(String name) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
    }

    public Category(String name, String alias) {
        this.name = name;
        this.alias = alias;
        this.image = "default.png";
    }

    public Category(String name, Category parent) {
        this.name = name;
        this.alias = name;
        this.image = "default.png";
        this.parent = parent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }
}
