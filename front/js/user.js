const token = localStorage.getItem("jwt-token")

if (token) {
  fetch("http://localhost:8080/v1/users", {
    method: "get",
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token, // Inclua o token no cabeçalho de autorização
    },
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data)
      const userList = document.getElementById("user-list")

      data.forEach((user) => {
        const listItem = document.createElement("li")
        const idNode = document.createTextNode(`ID: ${user.id}`)
        const nameNode = document.createTextNode(`Name: ${user.name}`)
        const loginNode = document.createTextNode(`Login: ${user.username}`)
        const emailNode = document.createTextNode(`E-mail: ${user.mail}`)

        listItem.appendChild(idNode)
        listItem.appendChild(document.createElement("br"))
        listItem.appendChild(nameNode)
        listItem.appendChild(document.createElement("br"))
        listItem.appendChild(loginNode)
        listItem.appendChild(document.createElement("br"))
        listItem.appendChild(emailNode)

        userList.appendChild(listItem)
      })
    })
    .catch((error) => {
      console.error("Error fetching user data:", error)
    })
} else {
  console.log("Token não encontrado")
}
