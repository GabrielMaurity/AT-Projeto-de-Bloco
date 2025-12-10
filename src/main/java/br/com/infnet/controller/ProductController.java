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
    private final SupplierService supplierService;

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
        model.addAttribute("suppliers", supplierService.getAll());
        return "form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable UUID id, Model model) {
        Product product = productService.getById(id);
        if (product == null) {
            throw new BusinessException("Produto não encontrado para edição");
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", Category.values());
        model.addAttribute("suppliers", supplierService.getAll());
        return "form";
    }

    @PostMapping("/save")
    public String saveProduct(@RequestParam String name,
                              @RequestParam BigDecimal price,
                              @RequestParam int stock,
                              @RequestParam Category category,
                              @RequestParam(required = false) UUID supplierId,
                              @RequestParam(required = false) UUID id, // Recebe o ID para edição
                              Model model) {
        try {
            // --- VALIDAÇÃO MANUAL OBRIGATÓRIA PARA O TESTE ---
            if (price.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("Preço deve ser maior que zero");
            }

            // Se vier ID, mantém. Se não, é null (novo produto)
            Product p = new Product(id, name, price, stock, category, supplierId);

            if (id != null) {
                productService.update(id, p); // Atualiza
            } else {
                productService.create(p); // Cria novo
            }
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
}