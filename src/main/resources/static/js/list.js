$(document).ready(function () {
        list();
        $("#delete").submit(function (event) {
            event.preventDefault();
            del_function();
        });
});

function list() {
        $.get('/books').done(function (books) {
            books.forEach(function (book) {
                $("tbody").append(`
                    <tr>
                        <td>${book.title}</td>
                        <td>${book.authors}</td>
                        <td>${book.genre}</td>
                        <td>${book.comments}</td>
                        <td>
                            <form action="/books/${book.id}" method="get">
                                <button type="submit">Edit</button>
                            </form>
                        </td>
                        <td>
                            <form action="/books/${book.id}?_method=delete" method="post">
                                <button type="submit">Delete</button>
                            </form>
                        </td>
                    </tr>
                `)
            });
        })
}
