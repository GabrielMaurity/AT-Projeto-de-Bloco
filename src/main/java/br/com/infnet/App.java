package br.com.infnet;

import br.com.infnet.controller.ProductController;
import br.com.infnet.service.ProductService;
import br.com.infnet.view.ProductView;

public class App
{
    public static void main( String[] args )
    {
        ProductService model = new ProductService();
        ProductView view = new ProductView();
        ProductController controller = new ProductController(model, view);

        controller.start();
    }
}
