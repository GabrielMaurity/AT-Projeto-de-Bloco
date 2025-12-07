package br.com.infnet.controller;

import br.com.infnet.exception.BusinessException;
import br.com.infnet.model.Category;
import br.com.infnet.model.Product;
import br.com.infnet.service.ProductService;
import br.com.infnet.service.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.UUID;

@Controller
public class ProductController {

    private final ProductService productService;
    private final SupplierService supplierService; // Injeção do novo serviço

    public ProductController(ProductService productService, SupplierService supplierService) {
        this.productService = productService;
        this.supplierService = supplierService;
    }

    @GetMapping("/")
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAll());
        return "list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("categories", Category.values());
        model.addAttribute("suppliers", supplierService.getAll()); // Passa fornecedores p/ tela
        return "form";
    }

    @PostMapping("/save")
    public String saveProduct(@RequestParam String name,
                              @RequestParam BigDecimal price,
                              @RequestParam int stock,
                              @RequestParam Category category,
                              @RequestParam(required = false) UUID supplierId, // Recebe o ID do fornecedor
                              Model model) {
        try {
            // Cria o objeto Product com o supplierId
            Product p = new Product(null, name, price, stock, category, supplierId);
            productService.create(p);
            return "redirect:/";
        } catch (BusinessException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("categories", Category.values());
            model.addAttribute("suppliers", supplierService.getAll());
            return "form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable UUID id) {
        productService.delete(id);
        return "redirect:/";
    }

    // Rota de teste chaos mantida
    @GetMapping("/chaos/timeout")
    public String triggerTimeout() {
        productService.simulateSlowDatabase();
        return "redirect:/";
    }
}