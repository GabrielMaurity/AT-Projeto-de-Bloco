package br.com.infnet.controller;

import br.com.infnet.model.Category;
import br.com.infnet.model.Product;
import br.com.infnet.service.ProductService;
import br.com.infnet.view.ProductView;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductController {
    private final ProductService service;
    private final ProductView view;

    public ProductController(ProductService service, ProductView view) {
        this.service = service;
        this.view = view;
    }

    public void start() {
        boolean running = true;
        while (running) {
            try {
                String option = view.showMenuAndGetOption();
                switch (option) {
                    case "1" -> createProduct();
                    case "2" -> listProducts();
                    case "3" -> findProduct();
                    case "4" -> updateProduct();
                    case "5" -> deleteProduct();
                    case "0" -> {
                        running = false;
                        view.showMessage("Encerrando...");
                    }
                    default -> view.showMessage("Opção inválida.");
                }
            } catch (Exception e) {
                view.showMessage("Erro: " + e.getMessage());
            }
        }
    }

    private void createProduct() {
        view.showMessage("--- Novo Produto ---");
        String name = view.askForString("Nome: ");
        BigDecimal price = view.askForBigDecimal("Preço: ");
        int stock = view.askForInt("Estoque: ");
        Category cat = view.askForCategory();

        UUID id = service.createProduct(name, price, stock, cat);
        view.showMessage("Sucesso! ID: " + id);
    }

    private void listProducts() {
        view.showProductList(service.getAllProducts(), service);
    }

    private void findProduct() {
        UUID id = view.askForUUID("ID: ");
        Product p = service.getProductById(id);
        if (p.equals(Product.NULL_PRODUCT)) {
            view.showMessage("Não encontrado.");
        } else {
            view.showProductDetails(p);
        }
    }

    private void updateProduct() {
        UUID id = view.askForUUID("ID para atualizar: ");
        if (service.getProductById(id).equals(Product.NULL_PRODUCT)) {
            view.showMessage("Produto não existe.");
            return;
        }
        String name = view.askForString("Novo Nome: ");
        BigDecimal price = view.askForBigDecimal("Novo Preço: ");
        int stock = view.askForInt("Novo Estoque: ");
        Category cat = view.askForCategory();

        service.updateProduct(id, name, price, stock, cat);
        view.showMessage("Atualizado com sucesso.");
    }

    private void deleteProduct() {
        UUID id = view.askForUUID("ID para deletar: ");
        service.deleteProduct(id);
        view.showMessage("Deletado.");
    }
}
