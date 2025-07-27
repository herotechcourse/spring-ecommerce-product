function productManager() {
    const API_ENDPOINT = "http://localhost:8080/api/products";

    const API = {
        async fetchProducts() {
            const res = await fetch(API_ENDPOINT);
            return res;
        },
        async create(name, price, imageUrl) {
            const payload = { name, price, imageUrl };

            const options = {
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            };

            const res = await fetch(API_ENDPOINT, { ...options, method: "POST" });
            return res;
        },
        async delete(id) {
            const res = await fetch(`${API_ENDPOINT}/${id}`, { method: "DELETE" });
            return res;
        },

        async update(id, name, price, imageUrl) {
            const payload = {};

            if (name) payload.name = name;
            if (price) payload.price = price;
            if (imageUrl) payload.imageUrl = imageUrl;

            const options = {
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            };

            const res = await fetch(`${API_ENDPOINT}/${id}`, { ...options, method: "PATCH" });
            return res;
        }
    }

    async function fetchProducts() {
        const res = await API.fetchProducts();
        if (res.status === 204) {
            document.getElementById("product-list").innerHTML = "No products.";
            return;
        }
        const products = await res.json();
        renderProducts(products);
    }

    function toggleForm(formId) {
        const forms = document.querySelectorAll("form");
        forms.forEach(form => form.classList.remove("active"));
        document.querySelector(`form[id=${formId}]`).classList.add("active");
    }

    function renderProducts(products) {
        const list = document.getElementById("product-list");
        list.innerHTML = '';
        products.forEach(product => {
            const div = document.createElement("div");
            div.innerHTML = `
                       <strong>${product.name}</strong> - $${product.price} <br>
                       <img src="${product.imageUrl}" alt="${product.name}" width="150" height="100"><br>
                       <button class="edit-button">Edit</button>
                       <button class="delete-button">Delete</button>
                       <hr/>
                   `;
            div.querySelector("button.edit-button").addEventListener("click", () => { handleProductEditButtonClick(product) })
            div.querySelector("button.delete-button").addEventListener("click", () => { deleteProduct(product.id) })

            list.appendChild(div);
        });
    }

    function getFormFields(form) {
        const id = form.querySelector("input[name=product-id]");
        const name = form.querySelector("input[name=product-name]");
        const price = form.querySelector("input[name=product-price]");
        const imageUrl = form.querySelector("input[name=product-image]");
        return { id, name, price, imageUrl };
    }

    function getFormValues(form) {
        const formFields = getFormFields(form);
        const id = formFields.id.value;
        const name = formFields.name.value || undefined;
        const price = formFields.price.value ? parseFloat(formFields.price.value): undefined;
        const imageUrl = formFields.imageUrl.value || undefined;
        return { id, name, price, imageUrl };
    }

    async function handleCreateProductSubmit(e) {
        e.preventDefault();

        const form = document.getElementById("product-form");
        const { name, price, imageUrl } = getFormValues(form);
        const res = await API.create(name, price, imageUrl)

        if (res.ok) {
            resetForm(form);
            fetchProducts();
        }
    }

    function handleProductEditButtonClick(product) {
        toggleForm("update-product-form");

        const form = document.getElementById("update-product-form");
        const formFields = getFormFields(form);

        formFields.id.value = product.id;
        formFields.name.value = product.name;
        formFields.price.value = product.price;
        formFields.imageUrl.value = product.imageUrl;
    }

    async function handleEditProductSubmit(e) {
        e.preventDefault();

        const form = document.getElementById("update-product-form");
        const { id, name, price, imageUrl } = getFormValues(form);
        const res = await API.update(id, name, price, imageUrl)

        if (res.ok) {
            toggleForm("product-form");
            resetForm(form);
            fetchProducts();
        }
    }


    async function deleteProduct(id) {
        const res = await API.delete(id);


        if (res.ok) {
            fetchProducts()
        }
    }


    function resetForm(form) {
        form.reset();
        form.querySelector("input[name=product-id]").value = "";
        form.querySelector("input[name=product-name]").value = "";
        form.querySelector("input[name=product-price]").value = "";
        form.querySelector("input[name=product-image]").value = "";
    }


    function setupCreateProductForm() {
        const form = document.getElementById("product-form");
        form.addEventListener("submit", handleCreateProductSubmit);
    }


    function setupEditProductForm() {
        const form = document.getElementById("update-product-form");
        form.addEventListener("submit", handleEditProductSubmit);


        const cancelEditButton = form.querySelector("button.cancel-edit-button");
        cancelEditButton.addEventListener("click", () => toggleForm("product-form"));
    }


    setupCreateProductForm();
    setupEditProductForm();


    fetchProducts();
}
