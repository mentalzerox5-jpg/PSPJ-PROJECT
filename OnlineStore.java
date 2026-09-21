import java.util.ArrayList;
import java.util.Scanner;

class Product {
    int id;
    String name;
    double price;
    int stock;

    Product(int id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }
}

class CartItem {
    Product product;
    int quantity;

    CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    double getTotal() {
        return product.price * quantity;
    }
}

class Order {
    int orderId;
    ArrayList<CartItem> items;
    double totalAmount;
    String status;

    Order(int orderId, ArrayList<CartItem> items) {
        this.orderId = orderId;
        this.items = new ArrayList<>(items);
        this.totalAmount = calculateTotal();
        this.status = "PLACED";
    }

    double calculateTotal() {
        double total = 0;

        for (CartItem item : items) {
            total += item.getTotal();
        }

        return total;
    }

    void displayOrder() {
        System.out.println("\n========== ORDER DETAILS ==========");
        System.out.println("Order ID: " + orderId);
        System.out.println("Status: " + status);

        for (CartItem item : items) {
            System.out.println(
                item.product.name + " x " +
                item.quantity + " = ₹" +
                item.getTotal()
            );
        }

        System.out.println("Total Amount: ₹" + totalAmount);
    }
}

public class OnlineStore {

    static ArrayList<Product> products = new ArrayList<>();
    static ArrayList<CartItem> cart = new ArrayList<>();
    static ArrayList<Order> orders = new ArrayList<>();

    static int nextOrderId = 1001;

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        products.add(new Product(1, "Laptop", 55000, 5));
        products.add(new Product(2, "Mouse", 800, 10));
        products.add(new Product(3, "Keyboard", 1500, 8));
        products.add(new Product(4, "Headphones", 2500, 6));

        int choice;

        do {
            System.out.println("\n========== ONLINE STORE ==========");
            System.out.println("1. View Products");
            System.out.println("2. Add Product to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Remove Product from Cart");
            System.out.println("5. Place Order");
            System.out.println("6. View Orders");
            System.out.println("7. Update Order Status");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    viewProducts();
                    break;

                case 2:
                    addToCart();
                    break;

                case 3:
                    viewCart();
                    break;

                case 4:
                    removeFromCart();
                    break;

                case 5:
                    placeOrder();
                    break;

                case 6:
                    viewOrders();
                    break;

                case 7:
                    updateOrderStatus();
                    break;

                case 8:
                    System.out.println("Thank you for using Online Store!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 8);

        sc.close();
    }

    // Display available products
    static void viewProducts() {

        System.out.println("\n========== PRODUCTS ==========");

        for (Product p : products) {
            System.out.println(
                "ID: " + p.id +
                " | " + p.name +
                " | Price: ₹" + p.price +
                " | Stock: " + p.stock
            );
        }
    }

    // Add product to cart
    static void addToCart() {

        viewProducts();

        System.out.print("\nEnter Product ID: ");
        int id = sc.nextInt();

        Product selected = findProduct(id);

        if (selected == null) {
            System.out.println("Product not found!");
            return;
        }

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();

        if (quantity <= 0) {
            System.out.println("Invalid quantity!");
            return;
        }

        if (quantity > selected.stock) {
            System.out.println("Not enough stock!");
            return;
        }

        // Check if product already exists in cart
        for (CartItem item : cart) {

            if (item.product.id == id) {

                if (item.quantity + quantity > selected.stock) {
                    System.out.println("Quantity exceeds available stock!");
                    return;
                }

                item.quantity += quantity;

                System.out.println("Product quantity updated in cart.");
                return;
            }
        }

        cart.add(new CartItem(selected, quantity));

        System.out.println("Product added to cart successfully!");
    }

    // Display cart
    static void viewCart() {

        if (cart.isEmpty()) {
            System.out.println("\nCart is empty.");
            return;
        }

        System.out.println("\n========== SHOPPING CART ==========");

        double total = 0;

        for (CartItem item : cart) {

            double itemTotal = item.getTotal();

            System.out.println(
                item.product.name +
                " x " + item.quantity +
                " = ₹" + itemTotal
            );

            total += itemTotal;
        }

        System.out.println("-----------------------------------");
        System.out.println("Cart Total: ₹" + total);
    }

    // Remove product
    static void removeFromCart() {

        if (cart.isEmpty()) {
            System.out.println("Cart is empty.");
            return;
        }

        viewCart();

        System.out.print("\nEnter Product ID to remove: ");
        int id = sc.nextInt();

        for (CartItem item : cart) {

            if (item.product.id == id) {
                cart.remove(item);
                System.out.println("Product removed from cart.");
                return;
            }
        }

        System.out.println("Product not found in cart.");
    }

    // Place order
    static void placeOrder() {

        if (cart.isEmpty()) {
            System.out.println("Cannot place order. Cart is empty.");
            return;
        }

        Order order = new Order(nextOrderId++, cart);

        // Reduce stock
        for (CartItem item : cart) {
            item.product.stock -= item.quantity;
        }

        orders.add(order);

        cart.clear();

        System.out.println("\nOrder placed successfully!");
        System.out.println("Your Order ID: " + order.orderId);
        System.out.println("Total Amount: ₹" + order.totalAmount);
        System.out.println("Order Status: " + order.status);
    }

    // View all orders
    static void viewOrders() {

        if (orders.isEmpty()) {
            System.out.println("\nNo orders found.");
            return;
        }

        for (Order order : orders) {
            order.displayOrder();
        }
    }

    // Update order status
    static void updateOrderStatus() {

        if (orders.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }

        System.out.print("Enter Order ID: ");
        int id = sc.nextInt();

        for (Order order : orders) {

            if (order.orderId == id) {

                switch (order.status) {

                    case "PLACED":
                        order.status = "CONFIRMED";
                        break;

                    case "CONFIRMED":
                        order.status = "PACKED";
                        break;

                    case "PACKED":
                        order.status = "SHIPPED";
                        break;

                    case "SHIPPED":
                        order.status = "DELIVERED";
                        break;

                    case "DELIVERED":
                        System.out.println("Order already delivered.");
                        return;
                }

                System.out.println(
                    "Order status updated to: " +
                    order.status
                );

                return;
            }
        }

        System.out.println("Order not found.");
    }

    // Find product by ID
    static Product findProduct(int id) {

        for (Product p : products) {

            if (p.id == id) {
                return p;
            }
        }

        return null;
    }
}