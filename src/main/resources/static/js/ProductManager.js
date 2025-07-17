function productManager() {

    const API = "http://localhost:8080/api/products";

    async function fetchProducts() {
        const res = await fetch(API);
        if (res.status === 204) {
            document.getElementById("product-list").innerHTML = "No products.";
            return;
        }
        const products = await res.json();
        renderProducts(products);
    }

    function renderProducts(products) {
        const list = document.getElementById("product-list");
        list.innerHTML = '';
        products.forEach(p => {
            const div = document.createElement("div");
            div.innerHTML = `
                        <strong>${p.name}</strong> - $${p.price} <br>
                        <img src="${p.imageUrl}" alt="${p.name}" width="150" height="100"><br>
                        <button onclick="editProduct(${p.id})">Edit</button>
                        <button onclick="deleteProduct(${p.id})">Delete</button>
                        <hr/>
                    `;
            list.appendChild(div);
        });
    }

    async function createOrUpdateProduct(e) {
        e.preventDefault();
        const id = document.getElementById("product-id").value;
        const name = document.getElementById("product-name").value;
        const price = parseFloat(document.getElementById("product-price").value);
        const imageUrl = document.getElementById("product-image").value;

        const payload = { name, price, imageUrl };
        const options = {
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        };

        if (id) {
            // Update
            const res = await fetch(`${API}/${id}`, { ...options, method: "PUT" });
            if (res.ok) {
                resetForm();
                fetchProducts();
            }
        } else {
            // Create
            const res = await fetch(API, { ...options, method: "POST" });
            if (res.ok) {
                resetForm();
                fetchProducts();
            }
        }
    }

    function editProduct(id) {
        fetch(`${API}`)
            .then(res => res.json())
            .then(data => {
                const product = data.find(p => p.id === id);
                document.getElementById("product-id").value = product.id;
                document.getElementById("product-name").value = product.name;
                document.getElementById("product-price").value = product.price;
                document.getElementById("product-image").value = product.imageUrl;
            });
    }

    async function deleteProduct(id) {
        const res = await fetch(`${API}/${id}`, { method: "DELETE" });
        if (res.ok) fetchProducts();
    }

    function resetForm() {
        document.getElementById("product-id").value = "";
        document.getElementById("product-form").reset();
    }

    document.getElementById("product-form").addEventListener("submit", createOrUpdateProduct);

    fetchProducts();
}