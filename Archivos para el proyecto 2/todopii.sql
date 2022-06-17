-- ================================================= --
-- Ervin Rodríguez Villanueva 
-- 2021095970
-- ================================================= --

drop database if exists todopii;
create database todopii;
use todopii;

-- Creación de tablas --
-- ================================================= --
create table clients(
	clientId varchar(50) primary key,
    clientSecret varchar(50) not null
);

create table sessions(
	sessionId varchar(50) primary key,
    clientId varchar(50),
    createdat timestamp not null,
    foreign key (clientId) references clients(clientId)
);

-- ================================================= --
-- Inserando algunos clientes
insert into clients(clientId,clientSecret) values ("cliente-1",'secret');
insert into clients(clientId,clientSecret) values ("cliente-2",'secret');
insert into clients(clientId,clientSecret) values ("cliente-3",'secret');
insert into clients(clientId,clientSecret) values ("cliente-4",'secret');
insert into clients(clientId,clientSecret) values ("cliente-5",'secret');
insert into clients(clientId,clientSecret) values ("cliente-6",'secret');

-- Insertando algunas sesiones
insert into sessions(sessionId,clientId,createdat) values("session-1","cliente-5",utc_timestamp());
insert into sessions(sessionId,clientId,createdat) values("session-2","cliente-4",utc_timestamp());
insert into sessions(sessionId,clientId,createdat) values("session-3","cliente-3",utc_timestamp());
insert into sessions(sessionId,clientId,createdat) values("session-4","cliente-2",utc_timestamp());


select * from clients;
select * from sessions;


-- ================================================= --
-- Procedimientos del video en clase

-- Si el cliente existe
select if(count(*) = 1,true,false) as cliente_id_exists from clients where clientId = 'cliente-13';

-- Si el cliente tiene una sesion
select if(count(*) = 1,true,false) as session_exists from sessions where sessionId = 'cliente-1';

-- Verificar si la session tiene más de 30 minutos de antiguedad
select clientId,SessionId,createdat, if(minute(timediff(utc_timestamp(),createdat)) <= 30, "ACTIVE","INACTIVE") as sessionStatus
from sessions order by createdat asc;

-- ================================================= --
-- Procesos Almacenados

-- Create_session

drop procedure if exists create_session;
delimiter $$
create procedure create_session(in client_id_param varchar(50), in session_ttl_param int)
begin
	declare client_id_exists bool;
    declare session_exists bool;
    declare session_diff int;
    
    select if(count(*) = 1,true,false) into client_id_exists from clients where clientId = client_id_param;
    
    if client_id_exists = true then
		-- select "Existe";
        select if(count(*) = 1,true,false) into session_exists from sessions where clientId = client_id_param;
		if session_exists then
			-- select "Cliente existe | sesion existe";
            select minute(timediff(utc_timestamp(),createdat)) into session_diff from sessions where clientId = client_id_param;
            
            if session_diff <= session_ttl_param then
				-- select "Cliente existe | sesion existe | sesion activa";
                select clientId,SessionId,createdat, if(timestampdiff(minute, createdat, utc_timestamp()) <= 30, "ACTIVE", "INACTIVE") as sessionStatus from sessions where clientId = client_id_param;
			else
				-- select "Cliente existe | sesion existe | sesion inactiva";
				start transaction;
					update sessions set createdat = utc_timestamp() where clientId = client_id_param;
				commit;
                select clientId,SessionId,createdat, if(timestampdiff(minute, createdat, utc_timestamp()) <= 30, "ACTIVE", "INACTIVE") as sessionStatus from sessions where clientId = client_id_param;
            end if;
		else
			-- select "Cliente existe | sesion No existe";
            start transaction;
				insert into sessions(clientId, sessionId, createdat) values(client_id_param,uuid(),utc_timestamp());
            commit;
            select clientId,SessionId,createdat, if(timestampdiff(minute, createdat, utc_timestamp()) <= 30, "ACTIVE", "INACTIVE") as sessionStatus from sessions where clientId = client_id_param;
        end if;
	else
        select "Cliente no existe";
	end if;
    -- select clientId,SessionId,createdat, if(minute(timediff(utc_timestamp(),createdat)) <= 10, "ACTIVE","INACTIVE") as sessionStatus from sessions where clientId = client_id_param;
end
$$
delimiter ;

-- ================================================= --
-- Ejemplos de uso create_session

-- Cliente no existe
call create_session('cliente-13',5);

-- Cliente existe, sesion existe
call create_session('cliente-2',10);

-- Cliente existe, sesion NO existe
call create_session('cliente-5',10);


-- ================================================= --
-- Segundo procedimiento almacenado

drop procedure if exists validate_session;
delimiter $$
create procedure validate_session(in session_id_param varchar(50))

begin
    declare session_exists bool;
	declare session_diff int;
    
	select if(count(*) = 1,true,false) into session_exists from sessions where sessionId = session_id_param;
	if session_exists then
		select minute(timediff(utc_timestamp(),createdat)) into session_diff from sessions where sessionId = session_id_param;
		if session_diff <= 30 then
			-- select 'sesion existe | sesion valida';
			select clientId,SessionId,createdat, if(timestampdiff(minute, createdat, utc_timestamp()) <= 30, "ACTIVE", "INACTIVE") as sessionStatus from sessions where sessionId = session_id_param;
		else
			-- select 'sesion existe | sesion invalida';
			select clientId,SessionId,createdat, if(timestampdiff(minute, createdat, utc_timestamp()) <= 30, "ACTIVE", "INACTIVE") as sessionStatus from sessions where sessionId = session_id_param;
        end if;
    else
		select 'sesion no existe';
	end if;
end;
$$
delimiter ;

call validate_session("session-1");

select * from clients;