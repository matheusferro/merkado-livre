create or replace function nested_categoryfun(in cname varchar(30), in pname varchar(30), inout msg TEXT)
language plpgsql
as $$
declare
newright bigint;
begin
select value_l into newright FROM category WHERE category_name = pname;
raise notice 'Valuel: %', newright;
UPDATE category SET value_r = value_r + 2 WHERE value_r > newright;
UPDATE category SET value_l = value_l + 2 WHERE value_l > newright;
INSERT INTO category(category_name, created_at, value_l, value_r) VALUES(cname, NOW(),newright + 1, newright + 2);
end; $$;

create or replace procedure nested_category(in cname varchar(30), in pname varchar(30))
language plpgsql
as $$
declare
newright bigint;
begin
select value_l into newright FROM category WHERE category_name = pname;
UPDATE category SET value_r = value_r + 2 WHERE value_r > newright;
UPDATE category SET value_l = value_l + 2 WHERE value_l > newright;
INSERT INTO category(category_name, created_at, value_l, value_r) VALUES(cname, NOW() ,newright + 1, newright + 2);
end; $$;

insert into category(category_name, created_at,  value_l, value_r) values('Computer', '2021-01-01',1,2);


call nested_category('Programming','Computer');
call nested_category('Graphics','Computer');
call nested_category('Databases','Computer');
call nested_category('Languages','Programming');
call nested_category('Javascript','Languages');
call nested_category('PHP','Languages');
call nested_category('Python','Languages');
call nested_category('Ruby','Languages');
call nested_category('Java','Languages');
call nested_category('Vector','Graphics');
call nested_category('Inkscape','Vector');
call nested_category('3D','Graphics');
call nested_category('Blender','3D');
call nested_category('Bitmapped','Graphics');
call nested_category('GIMP','Bitmapped');
call nested_category('Krita','Bitmapped');
call nested_category('MyPaint','Bitmapped');
call nested_category('SQL','Databases');
call nested_category('MySQL','SQL');
call nested_category('MS SQL','SQL');
call nested_category('PostgreSQL','SQL');
call nested_category('Routines','SQL');
call nested_category('Procedures','Routines');
call nested_category('Functions','Routines');
call nested_category('Triggers','SQL');
call nested_category('NoSQL','Databases');
call nested_category('MongoDB','NoSQL');
call nested_category('Casandra','NoSQL');
call nested_category('Sphinx','NoSQL');

--mysql
set sql_mode='STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
SELECT CONCAT( REPEAT( '  ', (COUNT(parent.cat_name) - 1) ), node.cat_name) AS name
FROM nested_cats AS node,
        nested_cats AS parent
WHERE node.lvalue BETWEEN parent.lvalue AND parent.rvalue
GROUP BY node.cat_name
ORDER BY node.lvalue;