const API_URL='/products';

function showAddProductForm() {
    document.getElementById('formTitle').innerText = 'Add Product';
    document.getElementById('productId').value='';
    document.getElementById('productName').value='';
    document.getElementById('productPrice').value='';
    document.getElementById('productImg').value='';
    document.getElementById('productQuantity').value='';
    document.getElementById('formSection').style.display='block';
}

function editProduct(id, name, price, img, quantity) {
    document.getElementById('formTitle').innerText = 'Edit Product';
    document.getElementById('productId').value=id;
    document.getElementById('productName').value=name;
    document.getElementById('productPrice').value=price;
    document.getElementById('productImg').value=img;
    document.getElementById('productQuantity').value=quantity;
    document.getElementById('formSection').style.display='block';
}

function cancelForm() {
    clearErrors()
    document.getElementById('formSection').style.display='none';
}

function submitForm(event) {
    event.preventDefault();

    const id=document.getElementById('productId').value;
    const product= {
        name: document.getElementById('productName').value,
        price: parseFloat(document.getElementById('productPrice').value),
        img: document.getElementById('productImg').value,
        quantity: parseInt(document.getElementById('productQuantity').value),
    };

    const method = id ? 'PUT' : 'POST';
    const url = id ? `${API_URL}/${id}` : API_URL

    fetch(url, {
        method: method,
        headers: {'Content-Type': 'application/json' },
        body: JSON.stringify(product)
    })
    .then(response => {
        if(!response.ok){
            return response.json().then(errors => {
                if (response.status === 400) {
                    displayErrors(errors.errors);
                } else if (response.status === 409 || response.status === 404) {
                    displayGlobalError(errors.error)
                }
                    throw new Error("Request failed");
                });
            }
        return response;
    })
    .then (() => {
        cancelForm();
        location.reload();
    })
    .catch(error => console.error(error));
}

function displayErrors(errors) {
    clearErrors();
    for (const field in errors) {
        const input = document.getElementById(`product${capitalize(field)}`);
        if (input) {
            const errorDiv = document.createElement("div");
            errorDiv.className = "error";
            errorDiv.style.color = "red";
            errorDiv.textContent = errors[field];
            input.parentNode.insertBefore(errorDiv, input.nextSibling);
        }
    }
}

function clearErrors() {
    document.querySelectorAll(".error").forEach(e => e.remove());
}

function displayGlobalError(message) {
    const formSection = document.getElementById('formSection');
    const errorDiv = document.createElement("div");
    errorDiv.className = "error";
    errorDiv.style.color = "red";
    errorDiv.style.marginBottom = "10px";
    errorDiv.textContent = message;
    formSection.insertBefore(errorDiv, formSection.firstChild);
}

function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

function deleteProduct(id) {
    if (!confirm('Are you sure?')) return;
        fetch(`${API_URL}/${id}`,{
        method: 'DELETE'}).then(() => location.reload());
}
