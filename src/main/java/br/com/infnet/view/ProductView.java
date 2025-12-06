package br.com.infnet.view;

import br.com.infnet.model.Category;
import br.com.infnet.model.Product;
import br.com.infnet.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class ProductView {
    private final Scanner scanner = new Scanner(System.in);

    public String showMenuAndGetOption() {
        System.out.println("\n=== MENU ===");
        System.out.println("1 - Criar | 2 - Listar | 3 - Buscar | 4 - Atualizar | 5 - Deletar | 0 - Sair");
        System.out.print("> ");
        return scanner.nextLine().trim();
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showProductList(List<Product> products, ProductService service) {
        if (products.isEmpty()) {
            System.out.println("Nenhum produto encontrado.");
            return;
        }
        System.out.println("--- Lista de Produtos ---");
        for (Product p : products) {
            // A View usa o Service apenas para formatar dados (ex: label da categoria), se necessário
            System.out.printf("[%s] %s - R$ %s (Estoque: %d) - %s%n",
                    p.id(), p.name(), p.price(), p.stock(), service.getCategoryLabel(p));
        }
    }

    public void showProductDetails(Product product) {
        System.out.println("Detalhes: " + product);
    }

    // Métodos de Input
    public String askForString(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }

    public BigDecimal askForBigDecimal(String label) {
        System.out.print(label);
        try {
            return new BigDecimal(scanner.nextLine().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor inválido.");
        }
    }

    public int askForInt(String label) {
        System.out.print(label);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Número inválido.");
        }
    }

    public UUID askForUUID(String label) {
        System.out.print(label);
        try {
            return UUID.fromString(scanner.nextLine().trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("ID inválido.");
        }
    }

    public Category askForCategory() {
        System.out.println("Categorias: " + java.util.Arrays.toString(Category.values()));
        System.out.print("Digite a categoria: ");
        try {
            return Category.valueOf(scanner.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Categoria inválida.");
        }
    }
}
