$(document).ready(function () {
    window.onload = () => showList();
    let mode = 'show';

    const showList = async function () {
        const list = [];
        await $.get("/api/product/", (data) => {
            data.forEach(x => {
                list.push(x);
            });
        });
        const bodyList = $(".body-list");
        $.each(bodyList, (x) => $(x).remove());
        list.forEach(x => {
            bodyList.append(`<div class="list-item text-bg-dark" id=${x.id}>
                                <div class="product-name">
                                    ${x.name}
                                    <i class="fa fa-pencil" aria-hidden="true"></i>
                                </div>
                                <div class="product-manage">
                                    <input class="form-check-input fa-lg" type="checkbox" ${!x.bought ? "" : 'checked'} id="flexCheckDefault">
                                </div>
                            </div>`);
        });
    }

    $(".nav-link").click((e) => {
        const oldSelectElement = $(".nav-link.active");
        oldSelectElement.removeClass("active");
        const newSelectElement = $(e.currentTarget);
        const pageName = newSelectElement.text();
        pageName.replaceAll('\n', '');
        pageName.trim(pageName);
        $(".body-title .title").text(pageName);
        newSelectElement.addClass("active");
    });

    $(".edit-btn .btn").click((e) => {
        if (mode === 'edit') {
            $(".add-btn").removeClass('active-btn');
            $(".list-item .product-manage .btn-danger").each(function () {
                $(this).addClass('remove');
            });
            setTimeout(function () {
                $(".list-item .product-manage .btn-danger").each(function () {
                    $(this).remove();
                });
            }, 100)
            mode = 'show';
        } else {
            $(".list-item .product-manage").each(function () {
                $(this).append(`<button type="button" class="btn btn-danger">Delete</button>`);
            });
            $(".add-btn").addClass('active-btn');
            mode = 'edit';
        }
    });

    $(".add-btn").click((e) => {
        const bodyList = $(".body-list");
        bodyList.prepend(`<div class="list-item text-bg-dark" id="123">
                            <div class="product-name">
                                <input type="text" class="form-control" placeholder="Name" aria-label="Recipient's username" aria-describedby="basic-addon2">
                            </div>
                            <div class="product-manage">
                                <button type="button" class="btn btn-danger">Add</button>
                            </div>
                        </div>`);
    });
});

$(document).on('click', '.btn-danger', (e) => {
    const element = $(e.currentTarget).closest('.list-item');
    const productId = $(element).attr('id');
    element.remove();
    $.ajax({
        url: `/api/product/${productId}`,
        type: 'DELETE'
    });
});

$(document).on('click', '.btn-success', async (e) => {
    const value = $(".form-control")[0].value;
    // console.log(value);
    const element = $(e.currentTarget).closest('.list-item');
    const item = {
        id: $(element).attr('id'),
        name: value,
        bought: false
    };


    const body = `{
        "id": -1,
        "name": "${value}",
        "bought": false
    }`;
    const id = await $.ajax({
        url: `/api/product/new`,
        type: 'POST',
        data: body,
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
    });

    element.remove();
    const bodyList = $(".body-list");
    bodyList.prepend(`<div class="list-item text-bg-dark" id=${id}>
                                <div class="product-name">
                                    ${item.name}
                                </div>
                                <div class="product-manage">
                                    <input class="form-check-input" type="checkbox" ${!item.bought ? "" : 'checked'} id="flexCheckDefault">
                                    <button type="button" class="btn btn-danger">Delete</button>
                                </div>
                            </div>`);
});

$(document).on('click', '.form-check-input', (e) => {
    const element = $(e.currentTarget).closest('.list-item');
    const productId = $(element).attr('id');
    $.post(`/api/product/${productId}/buy`);
});