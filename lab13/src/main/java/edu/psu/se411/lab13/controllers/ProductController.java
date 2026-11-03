package edu.psu.se411.lab13.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.psu.se411.lab13.model.Product;
import edu.psu.se411.lab13.services.ProductService;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	/** GET /api/v1/products : all products */
	@GetMapping()
	public ResponseEntity<Iterable<Product>> findAll() {
		return ResponseEntity.ok(productService.findAll());
	}

	/** GET /api/v1/products/{id} : 200 with the product, 404 if it does not exist, 400 if id is not positive */
	@GetMapping("/{id}")
	public ResponseEntity<Product> findById(@PathVariable @Positive Integer id) {
		return productService.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}
}
