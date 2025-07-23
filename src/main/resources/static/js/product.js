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
    }).then(() => {
        cancelForm();
        location.reload()
    });
}

function deleteProduct(id) {
    if (!confirm('Are you sure?')) return;
        fetch(`${API_URL}/${id}`,{
        method: 'DELETE'}).then(() => location.reload());
}
