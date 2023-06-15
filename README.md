# Práticas tecnológicas - API

Esse projeto foi criado para atender a parte prática do curso de boas práticas de implementação de API. 


## Deploy

Para fazer o deploy desse projeto rode

```bash
  mvn spring-boot:run
```

Para executar os testes unitários desse projeto rode

```bash
  mvn test
```
Documentação da API
http://localhost:8053/swagger-ui/index.html

## Instruções para prova
- Criar uma migration para a tabela USUARIO no schema API. Pode ser no mesmo arquivo
  da migration de cliente ou se preferir pode criar em outro arquivo.
  Se for criar em outro arquivo, deve ser mantido o pré-fixo do arquivo V1_0__
  incrementando os números da versão. Essa tabela deve ter os campos NOME,
  LOGIN, SENHA, EMAIL e PERMISSOES. O campo permissões é varchar e vai gravar
  as permissões separadas por vírgula. Nessa migration deve ser feita também uma carga
  contendo 2 usuários. O primeiro vai ter o login root e a senha 12345. A senha vai ser gravada
  criptografada pela classe BCryptPasswordEncoder. O sergundo usuário pode ter qualquer login
  Esse segundo usuário vai ser usado somente para o teste de exclusão de usuário.

- Alterar o endpoint get-token para buscar o usuário pelo login do request e
  comparar a senha do request com a senha criptografada no banco.
  Método para comparar a senha:
  BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
  encoder.matches(request.getPassword(), usuarioEncontrado.getSenha())
  o método acima retorna true se a senha tiver correta e false se a senha estiver errada.
- A senha do usuário deve ser criptografada antes de ser salva. Para criptografar a senha faça encoder.encode("12345"); Esse método retorna uma string com a senha criptografada
- Lembrando de encoder é uma instância de BCryptPasswordEncoder

- Criar endpoints para manter usuários
  [GET] /api/usuario Retorna todos os usuários com paginação | permissão: LEITURA_USUARIO
  
- [POST] /api/usuario Cria um usuário novo | permissão: ESCRITA_USUARIO
  
- [PUT] /api/usuario Altera um usuário | permissão: ESCRITA_USUARIO
  
- [DELETE] /api/usuario/{id} Exclui um usuário | permissão: ESCRITA_USUARIO
  
- [GET] /api/usuario/{id} Retorna um usuário | permissão: LEITURA_USUARIO

Depois que fizer a prova, pode rodar os testes da classe UsuarioControllerTest
para conferir se os testes estão passando.