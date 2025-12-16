-- Cria o usuário 'admin' caso ele ainda não exista
-- O '%' indica que o usuário pode se conectar a partir de qualquer host
-- A senha definida para o usuário será '123'
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY '123';

-- Concede TODOS os privilégios no banco 'mydb' para o usuário 'admin'
-- Isso permite criar, ler, atualizar e remover tabelas e registros
GRANT ALL PRIVILEGES ON mydb.* TO 'admin'@'%';

-- Recarrega as permissões do MySQL
-- Garante que as alterações de usuários e privilégios entrem em vigor imediatamente
FLUSH PRIVILEGES;
