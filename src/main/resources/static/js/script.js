const searchContacts = () => {
  let query = $("#search-input").val();
  $(".search-result").hide();

  if (query == "") {
    $(".search-result").hide();
  } else {
    console.log(query);

    let url = `http://localhost:8080/search/${query}`;

    fetch(url)
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        var text = `<div class='list-group'>`;
        data.forEach((contacts) => {
          text += `<a class='list-group-item list-group-action'>${contacts.conName}</a>`;
        });
        text += `</div>`;
        $(".search-result").html(text);
        $(".search-result").show();
      });
  }
};
