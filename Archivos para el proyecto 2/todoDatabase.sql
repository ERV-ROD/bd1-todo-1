

-- Crear base de datos de Todo

drop database if exists todo;

create database todo;

use todo;

-- Crear tabla todorecord

create table todorecord(
    tid varchar(50) primary key,
    title varchar(200) not null,
    description varchar(255),
    -- state varchar(100) not null default 'new',
    state enum('new', 'in_progress', 'blocked', 'finished') not null,
    startdate timestamp not null,
    enddate timestamp
);

delete from todorecord;



insert into todorecord(tid, title, description, state, startdate, enddate) values ('todo-1', 'Salir a correr', 'Intentar correr', 'New', now(), "2022-10-13");
insert into todorecord(tid, title, description, state, startdate, enddate) values ('todo-2', 'Terminar Tarea', 'Hacer las tareas', 'new', now(), "2022-06-10");
insert into todorecord(tid, title, description, state, startdate, enddate) values ('todo-3', 'Leer un libro', 'Dedicar una hora a leer', 'new', now(), "2022-07-13");
insert into todorecord(tid, title, description, state, startdate, enddate) values ('todo-4', 'Preparar un sandwich', 'Desayuno practico', 'new', "2022-09-13", "2022-09-14" );
insert into todorecord(tid, title, description, state, startdate, enddate) values ('todo-5', 'Ponerme zapatos', 'Este es mi quinto todo', 'blocked', "2022-03-13", "2022-04-15");

select * from todorecord where startdate > "2021-09-13 00:00:00" and startdate < "2022-6-13 00:00:00";

-- CRUD
-- Create (inserción), Read (select), Update (update), Delete (delete)


select * from todorecord where state = 'blocked';

select * from todorecord where startdate > '2021-03-13' and startdate <  '2022-04-8' ;


select * from todorecord where title like "%cuarto%";

select * from todorecord;