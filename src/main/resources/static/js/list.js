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
                            <form action="/edit/${book.id}" method="post">
                                <button type="submit">Edit</button>
                            </form>
                        </td>
                        <td>
                            <form action="/delete/${book.id}" method="post">
                                <button type="submit">Delete</button>
                            </form>
                        </td>
                    </tr>
                `)
            });
        })
}
