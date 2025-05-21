Para utilização desta aplicação é necessário:


- Editor/Editores que possam ser utilizados as frameworks React e Spring Boot.
- Instalação de dependencias npm e Vite (React).
- Instalar MySql

Após as tecnologias estarem instaladas o processo para utilizar é:

Frontend:
- Aceder no cmd a pasta definida. Ex: cd Auditoria Norma 27001 (se estiver na pasta utilizador)
- De seguida npm run dev
- Carregar na tecla "o" para abrir a pagina no browser.
- Devera aparecer o frontend.

Backend:
- Importar projeto como "Maven".
- Criar uma enviroment variable com o nome "GEMINI_API_KEY" e inserir a sua chave de API Gemini ou no ficheiro aplication.properties substituir diretamente.
- Altere no aplication.properties onde diz "passwordmysql" para a sua password definida no Mysql e caso necessario modifique o "root" para o seu nome de utilizador definido.
- Run AuditoriaApplication.java
  
MySQL:
- Criar uma base de dados com o nome presente no aplication.properties
- Adicionar todos os ficheiros da base de dados presentes, ja contem alguns recursos.

Feitos estes passos a aplicação deve funcionar corretamente.

Caso queira testar a pagina de recursos utilize o postman para adicionar um recurso a escolha.
Como fazer com exemplos:
Documentos
![image](https://github.com/user-attachments/assets/a07b0244-c1ad-4f1e-99ca-0a3b30d5ed07)

Videos:

![image](https://github.com/user-attachments/assets/feda919f-021b-4f3f-b6ed-d9269ae412e4)

Cursos:

![image](https://github.com/user-attachments/assets/6971f4cf-64b9-4494-8ea7-a5670b92b999)

Se aparecer o codigo 201 no postman os recursos irão aparecer no programa.
