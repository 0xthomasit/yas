package com.yas.product.viewmodel.product;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yas.product.model.enumeration.DimensionUnit;
import com.yas.product.validation.ValidateProductPrice;
import com.yas.product.viewmodel.productoption.ProductOptionValuePostVm;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

// All fields, regardless of their access modifier (public, private, protected, or package-private),
// will be automatically detected and considered for inclusion in the JSON output or for mapping from JSON input.
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize // used to specify custom serializer to marshall the json object.
public record ProductPostVm(
        @NotBlank String name,
        @NotBlank String slug,
        Long brandId,
        List<Long> categoryIds,
        String shortDescription,
        String description,
        String specification,
        String sku,
        String gtin,
        Double weight,
        DimensionUnit dimensionUnit,
        Double length,
        Double width,
        Double height,
        @ValidateProductPrice Double price,
        Boolean isAllowedToOrder,
        Boolean isPublished,
        Boolean isFeatured,
        Boolean isVisibleIndividually,
        Boolean stockTrackingEnabled,
        String metaTitle,
        String metaKeyword,
        String metaDescription,
        Long thumbnailMediaId,
        List<Long> productImageIds,
        List<ProductVariationPostVm> variations,
        List<ProductOptionValuePostVm> productOptionValues,
        List<ProductOptionValueDisplay> productOptionValueDisplays,
        List<Long> relatedProductIds,
        Long taxClassId) implements ProductSaveVm<ProductVariationPostVm> {
}
