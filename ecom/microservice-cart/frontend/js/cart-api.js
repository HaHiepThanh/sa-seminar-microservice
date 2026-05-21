document.addEventListener("DOMContentLoaded", async function () {
    const API_BASE = "http://localhost:8080";
    const TOKEN_KEY = "ministore_jwt";
    const USER_KEY = "ministore_user";
    const ERROR_PAGE = "error.html";

    function goToError(code, message) {
        window.location.href = `${ERROR_PAGE}?code=${code}&msg=${encodeURIComponent(message)}`;
    }

    const cartTbody = document.querySelector(".table tbody");
    if (!cartTbody) return;

    cartTbody.innerHTML = `<tr><td colspan="4" class="text-center py-5">Đang kiểm tra phiên đăng nhập...</td></tr>`;

    let jwtToken = localStorage.getItem(TOKEN_KEY);
    let currentUser = localStorage.getItem(USER_KEY) || "admin";

    if (!jwtToken) {
        console.log("Tiến hành tự động lấy token ngầm cho FE...");
        try {
            const res = await fetch(`${API_BASE}/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username: "admin", password: "123456" })
            });
            if (res.ok) {
                const data = await res.json();
                jwtToken = data.access_token;
                currentUser = "admin";
                localStorage.setItem(TOKEN_KEY, jwtToken);
                localStorage.setItem(USER_KEY, currentUser);
                await loadCart();
            } else {
                cartTbody.innerHTML = `<tr><td colspan="4" class="text-center py-5 text-danger">Tự động lấy token thất bại (Backend từ chối)</td></tr>`;
            }
        } catch (e) {
            cartTbody.innerHTML = `<tr><td colspan="4" class="text-center py-5 text-danger">Không kết nối được Backend để lấy token</td></tr>`;
        }
        return;
    }
    loadCart();

    async function loadCart() {
        try {
            cartTbody.innerHTML = `<tr><td colspan="4" class="text-center py-3">Đang tải giỏ hàng...</td></tr>`;
            const res = await fetch(`${API_BASE}/api/carts/${currentUser}`, {
                headers: { "Authorization": `Bearer ${jwtToken}` }
            });

            if (res.status === 401) {
                handleUnauthorized();
                return;
            }
            if (!res.ok) goToError(res.status, "Không thể tải giỏ hàng từ server.");
            const items = await res.json();

            if (items.length === 0) {
                cartTbody.innerHTML = `<tr><td colspan="4" class="text-center py-5 text-muted">Giỏ hàng của bạn đang trống 🛒</td></tr>`;
                return;
            }
            renderCart(items);
        } catch (e) {
            console.error(e);
            goToError(500, "Không thể kết nối đến server.");
        }
    }
    function handleUnauthorized() {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(USER_KEY);
        jwtToken = "";
        cartTbody.innerHTML = `
            <tr>
                <td colspan="4" class="text-center py-5">
                    <h5 class="text-warning">⚠️ Đang lấy lại token mới... Vui lòng F5 (Reload)</h5>
                </td>
            </tr>`;
    }

    function renderCart(items) {
        cartTbody.innerHTML = "";
        let total = 0;

        items.forEach((item) => {
            const cartId = item.id;
            const productName = item.productName || item.productId;
            const qty = item.quantity;
            const price = item.price || 0;
            const subtotal = price * qty;
            total += subtotal;
            const imgSrc = item.imageUrl || "images/product-item1.jpg";

            const tr = document.createElement("tr");
            tr.innerHTML = `
                <td class="py-4 px-4 border-bottom border-light">
                    <div class="d-flex align-items-center">
                        <img src="${imgSrc}" class="img-fluid rounded-4 me-3 shadow-sm" style="width: 80px; height: 80px; object-fit: cover;">
                        <div>
                            <h5 class="mb-1 fs-6 fw-bold"><a href="#" class="text-dark text-decoration-none text-uppercase">${productName}</a></h5>
                            <a href="#" class="text-danger small text-decoration-none fw-bold btn-remove" data-id="${cartId}">Remove</a>
                        </div>
                    </div>
                </td>
                <td class="py-4 border-bottom border-light">
                    <span class="fs-5 fw-bold text-dark">$${price.toFixed(2)}</span>
                </td>
                <td class="py-4 border-bottom border-light">
                    <div class="input-group bg-white rounded-pill overflow-hidden custom-quantity-wrapper" style="width: 120px;">
                        <button class="btn text-dark px-2 fw-bold shadow-none fs-5 btn-minus" type="button" data-id="${cartId}" data-qty="${qty}">-</button>
                        <input type="number" class="form-control text-center shadow-none fw-bold p-0 fs-6 qty-input" value="${qty}" min="1" readonly style="background: transparent;">
                        <button class="btn text-dark px-2 fw-bold shadow-none fs-5 btn-plus" type="button" data-id="${cartId}" data-qty="${qty}">+</button>
                    </div>
                </td>
                <td class="py-4 px-4 text-end border-bottom border-light">
                    <span class="fs-5 fw-bolder text-primary subtotal-val">$${subtotal.toFixed(2)}</span>
                </td>
            `;
            cartTbody.appendChild(tr);
        });

        // Cập nhật tổng
        const totalElems = document.querySelectorAll(".fs-3.fw-bolder.text-primary, .fs-5.text-dark.fw-bolder");
        totalElems.forEach(el => { if (el.textContent.includes("$")) el.innerText = "$" + total.toFixed(2); });

        // Gắn sự kiện
        document.querySelectorAll(".btn-plus").forEach(btn => btn.addEventListener("click", handleUpdateQty));
        document.querySelectorAll(".btn-minus").forEach(btn => btn.addEventListener("click", handleUpdateQty));
        document.querySelectorAll(".btn-remove").forEach(btn => btn.addEventListener("click", handleRemove));
    }

    async function handleUpdateQty(e) {
        const btn = e.target;
        const cartId = btn.getAttribute("data-id");
        const isPlus = btn.classList.contains("btn-plus");
        const input = btn.parentNode.querySelector(".qty-input");
        let qty = parseInt(input.value, 10);

        if (isNaN(qty)) qty = 1;
        if (isPlus) qty++;
        else qty--;
        if (qty < 1) return;

        input.setAttribute("disabled", true);
        try {
            const res = await fetch(`${API_BASE}/api/carts/${cartId}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json", "Authorization": `Bearer ${jwtToken}` },
                body: JSON.stringify({ quantity: qty })
            });
            if (res.status === 401) { handleUnauthorized(); return; }
            if (res.ok) {
                const updated = await res.json();
                input.value = updated.quantity;
                if (updated.warning) console.warn("⚠️", updated.warning);
                await loadCart();
            } else {
                goToError(res.status, "Không thể cập nhật số lượng.");
            }
        } catch (err) {
            console.error(err);
        } finally {
            input.removeAttribute("disabled");
        }
    }

    async function handleRemove(e) {
        e.preventDefault();
        const cartId = e.target.getAttribute("data-id");
        e.target.innerText = "Đang xoá...";
        try {
            const res = await fetch(`${API_BASE}/api/carts/${cartId}`, {
                method: "DELETE",
                headers: { "Authorization": `Bearer ${jwtToken}` }
            });
            if (res.status === 401) { handleUnauthorized(); return; }
            if (res.ok) await loadCart();
            else goToError(res.status, "Không thể xoá sản phẩm khỏi giỏ hàng.");
        } catch (err) { console.error(err); }
    }
});
