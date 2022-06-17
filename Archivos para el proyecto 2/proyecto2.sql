-- ================================================= --
-- Ervin Rodríguez Villanueva 
-- 2021095970
-- ================================================= --

drop database todosocial;
create database todosocial;

use todosocial;

create table rating(
	id int primary key auto_increment,
    ratingvalue int not null,
    todoid varchar(50) not null,
    clientid varchar(50) not null,
    createdat datetime not null
);


-- ----------- Datos de prueba -------------------

insert into rating(ratingvalue, todoid, clientid, createdat) values(4, "todo-id", "client-id",now());
insert into rating(ratingvalue, todoid, clientid, createdat) values(4, "todo-id", "client-id",now());
insert into rating(ratingvalue, todoid, clientid, createdat) values(5, "todo-id", "client-id",now());
select * from rating;

-- -----------------------------------------------------
-- Procedimiento almacenado para calcular el Promedio --
-- -----------------------------------------------------
delimiter $$
create procedure calculate_rating_average(in todoidParam varchar(50))
begin
	select avg(ratingValue) as ratingAvg from rating where todoid = todoidParam;
end $$
delimiter ;

-- Prueba de procedimiento
call calculate_rating_average("todo-id");

-- -----------------------------------------------------
-- Procedimiento almacenado para registrar un rating  --
-- -----------------------------------------------------

drop procedure if exists rate_todo;
delimiter $$
	create procedure rate_todo(in clientIdParam varchar(50), in todoIdParam varchar(50) , in createAtParam datetime , in ratingParam int)
	begin
        declare rating_exists bool;
		select if(count(*) >= 1,true,false) into rating_exists from rating where clientId = clientIdParam;
		if rating_exists then
			select "El cliente ya calificó este Todo";
		else 
			insert into rating(ratingvalue, todoid, clientid, createdat) values(ratingParam, todoIdParam, clientIdParam,createAtParam);
            select * from rating where todoid = todoIdParam;
        end if;
    end$$

delimiter ;

-- Prueba de procedimiento
call rate_todo("client-149","todo-id",now(),5);
select * from rating;

-- ----------------------------------------------------
--   Procedimiento almacenado para borrar un rating    --
-- ----------------------------------------------------

drop procedure if exists delete_rating;
delimiter $$
	create procedure delete_rating(in todoIdParam varchar(50), in clientIdParam varchar(50))
	begin
        declare rating_exists bool;
		select if(count(*) >= 1,true,false) into rating_exists from rating where todoId = todoIdParam and clientId = clientIdParam;
		if rating_exists then
			delete from rating where todoid = todoIdParam and clientId = clientIdParam;
            select "Rating eliminado"  as status;
		else 
			select "Este rating no existe" as status;
        end if;
    end$$

delimiter ;

-- Prueba para borrar un rating

call delete_rating("todo-2","client-13");

select * from rating;

-- =================================================================== --

create table review(
	id int primary key auto_increment,
    todoid varchar(50) not null,
	createdat datetime not null,
    reviewText varchar(200) not null,
    clientid varchar(50) not null
);


-- ----------------------------------------------------------
-- Procedimiento almacenado para obtener todos los review  --
-- ----------------------------------------------------------

drop procedure if exists get_all_reviews;
delimiter $$
	create procedure get_all_reviews(in todoIdParam varchar(50))
	begin
		select * from review where todoId = todoIdParam;
    end$$
delimiter ;

call get_all_reviews("todo-2");

-- -----------------------------------------------------
-- Procedimiento almacenado para registrar un review  --
-- -----------------------------------------------------

drop procedure if exists add_review;
delimiter $$
	create procedure add_review(in clientIdParam varchar(50), in todoIdParam varchar(50),in reviewTextParam varchar(200), in createAtParam datetime)
	begin
		declare comment_exists bool;
		select if(count(*) >= 1,true,false) into comment_exists from review where clientId = clientIdParam;
        if(comment_exists) then
			select "El usuario ya proporcionó un comentario" as status;
		else
			insert into review(todoId, clientid, reviewText, createdat) values(todoIdParam, clientIdParam, reviewTextParam, createAtParam);
            select * from review where clientid = clientidParam;
        end if;
    end$$
delimiter ;

call add_review("client-2","todo-2","Es importante no olvidar esto",now());

call add_review("client-3","todo-1","No todos los viernes son buenos",now());

call add_review("client-4","todo-2","Interesante todo",now());


-- -----------------------------------------------------
--  Procedimiento almacenado para eliminar un review  --
-- -----------------------------------------------------

drop procedure if exists delete_review;
delimiter $$
	create procedure delete_review(in clientIdParam varchar(50),in todoIdParam varchar(50))
	begin
        declare review_exists bool;
		select if(count(*) >= 1,true,false) into review_exists from review where todoId = todoIdParam and clientId = clientIdParam;
		if review_exists then
			delete from review where todoid = todoIdParam and clientId = clientIdParam;
            select "Review eliminado"  as status;
		else 
			select "Este review no existe" as status;
        end if;
    end$$

delimiter ;

call delete_review("client-1300","todo-2");

-- -------------------------------------------------------
--  Procedimiento almacenado para actualizar un review  --
-- -------------------------------------------------------

drop procedure if exists update_review;
delimiter $$
	create procedure update_review(in clientIdParam varchar(50), in todoIdParam varchar(50),in reviewTextParam varchar(200),in createAtParam datetime)
	begin
		declare comment_exists bool;
		select if(count(*) >= 1,true,false) into comment_exists from review where todoId = todoIdParam and clientId = clientIdParam;
        if(comment_exists) then
			update review set reviewText = reviewTextParam, createdAt = createAtParam where todoId = todoIdParam and clientId = clientIdParam;
            select * from review where todoId = todoIdParam and clientId = clientIdParam;
		else
			select "El review no existe" as status;
        end if;
    end$$
delimiter ;

call update_review("client-4","todo-2","Valió la pena este review, casi salgo sin zapatos",now());

select * from review;




