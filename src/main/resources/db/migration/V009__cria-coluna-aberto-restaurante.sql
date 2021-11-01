alter table restaurante add aberto tinyint(1) not null default 1;
update restaurante set aberto = false;
