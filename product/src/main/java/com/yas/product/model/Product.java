package com.yas.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yas.commonlibrary.model.AbstractAuditEntity;
import com.yas.product.model.attribute.ProductAttributeValue;
import com.yas.product.model.enumeration.DimensionUnit;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product")
// IMPORTANT: Maps this class to the "product" database table. Ensures the entity is persisted correctly.
@lombok.Getter
@lombok.Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SuppressWarnings("javaarchitecture:S7027")
public class Product extends AbstractAuditEntity {

    // Inherits all 4 audit fields (in AbstractAuditEntity) automatically!

    @OneToMany(mappedBy = "product")
    @Builder.Default
    List<ProductRelated> relatedProducts = new ArrayList<>();

    // IMPORTANT: Auto-generates the primary key using database identity. Efficient for most RDBMS like PostgreSQL.
    // 🔑 PRIMARY KEY - Unique identifier for each product
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 📝 BASIC PRODUCT INFO
    private String name; // Product name (e.g., "iPhone 15 Pro")
    private String sku; // Stock Keeping Unit - unique identifier
    private String gtin; // Global Trade Item Number (barcode)
    private String slug; // URL-friendly name (e.g., "iphone-15-pro")
    private Double price; // Product price

    // 🎨 PRODUCT DETAILS
    private String shortDescription; // Brief description for listings
    private String description; // Full product description
    private String specification; // Technical specifications

    // 🏷️ PRODUCT RELATIONSHIPS
    @ManyToOne // Many products can belong to one brand
    @JoinColumn(name = "brand_id")
    private Brand brand; // e.g., Apple, Samsung

    // 📂 CATEGORY MAPPING (One product can be in multiple categories)
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST})
    @Builder.Default
    private List<ProductCategory> productCategories = new ArrayList<>();

    // 🖼️ MEDIA MANAGEMENT
    private Long thumbnailMediaId; // Main product image
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST})
    @Builder.Default
    private List<ProductImage> productImages = new ArrayList<>(); // Gallery images

    // 🎛️ PRODUCT VARIANTS (e.g., iPhone in different colors/storage)
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Product parent; // Link to parent product

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    @JsonIgnore
    @Builder.Default
    private List<Product> products = new ArrayList<>(); // Child variants

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    @Builder.Default
    private List<ProductAttributeValue> attributeValues = new ArrayList<>();

    // 📊 PRODUCT STATUS FLAGS
    private boolean hasOptions; // Does product have variants?
    private boolean isAllowedToOrder; // Can customers order this?
    private boolean isPublished; // Is product visible to customers?
    private boolean isFeatured; // Show on homepage?
    private boolean isVisibleIndividually; // Show as separate product?
    private boolean stockTrackingEnabled; // Track inventory?

    // 📦 INVENTORY
    private Long stockQuantity;       // How many items in stock

    // 💰 TAX & PRICING
    private Long taxClassId;          // Tax category
    private boolean taxIncluded;      // Is tax included in price?

    // 📏 PHYSICAL DIMENSIONS (for shipping calculations)
    private Double weight;

    @Enumerated(EnumType.STRING)
    private DimensionUnit dimensionUnit;  // CM or INCH
    private Double length;
    private Double width;
    private Double height;

    // 🔍 SEO (Search Engine Optimization)
    private String metaTitle;         // Title for search engines
    private String metaKeyword;       // Keywords for SEO
    private String metaDescription;   // Description for search results

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
}