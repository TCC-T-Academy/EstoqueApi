# WebStock 

Este repositório contém um projeto *full-stack* usando *Spring Boot* e *Angular*.

O projeto é um trabalho avaliativo proposto pelo programa de treinamento *T-Academy*, ocorrido entre 
03/10/2022 e 15/12/2022, e promovido pela [T-Systems do Brasil](https://www.t-systems.com/br/pt) em parceria com a [ProWay](https://www.proway.com.br/).

## Tecnologias e ferramentas necessárias
- Java 17
- Angular
- Gerenciador de pacotes para Node.js (`npm`)
- Maven
- MySQL Workbench

## Regras de negócio
- O projeto representa um **sistema de gerenciamento de estoque** para produção.
- Todas as empresas precisam comprar **insumos** para a fabricação de seus produtos.
- As empresas também compram **produtos** utilizados no cotidiano do negócio, mas sem relação com a 
  produção. Um exemplo desse tipo de produto são os materiais de escritório.
- Esses itens comprados, sejam insumos ou produtos, precisam ser armazenados de forma a facilitar sua utilização 
  quando for conveniente.
- Para isso, faz-se necessário haver um registro de quantidades de cada item, sua disponibilidade atual e futura, e as 
  datas de 
  entradas e saídas de qualquer quantidade desses itens.
> Um sistema de gerenciamento de estoque visa tornar a produção mais eficiente devido à facilidade e 
> rapidez com que se pode  dispor dos produtos ou insumos, e evitar o risco de quebra de estoque, que pode ter consequências 
> graves para a produção e para todo o negócio.
- A lógica de negócio foi organizada em 6 unidades de informação (classes Java, doravante também chamadas de 
  *entidades*), que podem ser pensadas como tabelas.
  - **Item**: um item se refere às informações descritivas de um determinado item como sua descrição e seu código único.
  - **Estoque**: o estoque guarda informação relacionada a um item via chave estrangeira (em uma relação *many-to-one*)
    , e também informações relativas à quantidade disponível daquele item, assim sua localização. Dessa forma, é possível 
    haver quantidades diferentes de um item em localizações diferentes, e como consequência disso, haverá mais de um 
    registro de estoque para um mesmo item. 
  - **Movimentação**: uma movimentação é um registro de entrada ou saída de quantidade de um determinado item no 
    estoque.
    Portanto, uma movimentação está relacionada a um item, a um registro de estoque, um tipo (*in/out*), a uma ordem 
    `origemDestino` (que pode ser uma **Ordem de Produção - OP**, ou **Ordem de Compra - OC**), e a uma quantidade a 
    ser adicionada (*in*) ou subtraída (*out*) do estoque, e a um usuário, que irá registrar essa movimentação.
  - **Reserva**: uma reserva é um registro de necessidade futura de uma quantidade de itens. Esse registro sempre 
    guarda 
    um data futura, e quando essa data chega, supõe-se que deva ser realizada uma movimentação que reduz a 
    quantidade em estoque de um item. As reservas sempre estão relacionadas a ordens de produção (OP). 
  - **Previsão**: uma previsão é um registro de chegada de uma quantidade de itens em datas futuras. Quando a data 
    chega, deve ser realizada uma movimentação que registra um aumento na quantidade em estoque de um determinado item. As 
    previsões sempre estão relacionadas a ordens de compra (OC).
  - **Usuário**: o registro de movimentações, reservas, e previsões deve ser realizado por um usuário.
   
## Diagramas de entidades e fluxo de atividades

O diagrama a seguir objetiva descrever as entidades e seus principais atributos:
![Entidades com seus principais atributos](/imagens/excalidraw-entidades.png)

O diagrama abaixo objetiva explicar o fluxo de atividades no sistema de gerenciamento de estoque.
![Fluxo de atividade WebStock](/imagens/excalidraw-fluxo.png)

## Utilização do repositório
- Após realizar o *fork* e *clone* do repositório localmente, recomenda-se abrir o projeto em uma IDE a partir do 
  arquivo `pom.xml`.
- Dessa forma, o Maven poderá fazer o *download* das dependências do projeto imediatamente.
- A seguir, é importante atentar-se aos perfis de projeto sob os quais se quer executar o sistema.
- Altere a propriedade `profiles` no arquivo `src/main/resources/application.properties` para `spring.profiles.
  active=dev` se for usar o perfil **DEV** (é necessário usar o MySQL Workbench) e `spring.profiles.active=test` 
  se decidir usar o perfil **TEST** (que usa o banco de dados *on-line* H2).
  - Perfil **TEST** (`src/main/resources/application-test.properties`):
    - Não é necessária nenhuma configuração manual. As tabelas serão geradas automaticamente no banco de dados H2 e
      excluídas ao fim da execução.
    - As tabelas geradas estarão vazias, ao contrário do que ocorre no perfil **DEV**.
    - Acesse o `localhost:8081`.
  - Perfil **DEV** (`src/main/resources/application-dev.properties`)
    - Execute o comando `CREATE SCHEMA estoque_api` no MySQL Workbench.
    - Verifique se o *username* e senha são os mesmos no seu MySQL local. Por padrão, no projeto é utilizado o 
      *username* **root**, com a senha também como **root**.
    - No arquivo `application-dev.properties`, editar a propriedade `ddl-auto` para `spring.jpa.hibernate.
      ddl-auto:create`.
    - Executar o projeto a partir do arquivo `src/main/java/com.estoqueapi.EstoqueApi/EstoqueApiApplication`. Assim, 
      o *Hibernate* poderá criar as tabelas e inserir dados conforme arquivo `src/main/resources/import.sql`.
    - No arquivo `application-dev.properties`, editar novamente a propriedade `ddl-auto` para `spring.jpa.hibernate.
      ddl-auto:none`.
    - Se a propriedade `ddl-auto` permanecer configurada como `create`, o banco de dados será sobrescrito a cada 
      execução do projeto.
    - Acesse o `localhost:8081`.

## Testes


## Equipe de desenvolvimento
- [Eduardo Hartwig](https://github.com/TheHirozuki)
- [Guilherme Weidman](https://github.com/gwdmnn)
- [Kelvin de Souza](https://github.com/ksouza08)
- [Leonardo Schulz](https://github.com/leonardowschulz)
- [Marília Ferreira](https://github.com/miachafer)
- [Sophia Testoni](https://github.com/SophiaTestoni)

## Métodos ágeis
- [Trello](https://trello.com/b/BPdDDJ6o/tcc-t-academy)

