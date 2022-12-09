USE estoque_api;
drop function if exists getIntAleatorio; 
delimiter $$
create function getIntAleatorio(idInicio bigint, idFim bigint, incremento int) 
returns bigint deterministic
begin
    declare res bigint; 
    declare resto bigint;
    if(incremento = 0) then
		set incremento = 1;
	end if;
	set res = round(rand() * (idFim - idInicio - incremento)) + idInicio;
	set resto = res mod incremento;
	while (resto <> 0) do
		set res = round(rand() * (idFim - idInicio - incremento)) + idInicio;
		set resto = res mod incremento;
	end while;
	return res;   
end$$
delimiter ;

drop function if exists getFloatAleatorio; 
delimiter $$
create function getFloatAleatorio(idInicio float, idFim float) 
returns float deterministic
begin
    declare res float; 
    set res = round(rand() * (idFim - idInicio - 1)) + idInicio;
	return res;	    
end$$
delimiter ;

drop procedure if exists gerarPrevisao;
delimiter $$
create procedure gerarPrevisao(in dias int)
begin
	declare qtd float;
    declare idItem bigint;
    declare compras int;
	declare idFim bigint;
    declare idInicio bigint;
    
    select max(id_item) into idFim from item;
    select min(id_item) into idInicio from item;
    
    set compras = (select COUNT(*) from previsao) + 1000;
        
	set qtd = getFloatAleatorio(50,100);    
    select getIntAleatorio(idInicio,idFim,10) into idItem;
    
    insert into previsao (data_prevista,finalizada,ordem,quantidade_prevista,item_id_item,usuario_id_usuario)
			values (date_add(now(), interval dias day),0,concat('CP',compras),qtd,idItem,1);
    
end$$
delimiter ;

drop procedure if exists gerarReserva;
delimiter $$
create procedure gerarReserva(in dias int)
begin
	declare qtd float;
    declare idItem bigint;
    declare po int;
	declare idFim bigint;
    declare idInicio bigint;
    
    select max(id_item) into idFim from item;
    select min(id_item) into idInicio from item;
    
    set po = (select COUNT(*) from reserva) + 1000;
        
	set qtd = getFloatAleatorio(50,100);    
    select getIntAleatorio(idInicio,idFim,10) into idItem;
    
    insert into reserva (data_prevista,finalizada,ordem,quantidade_reserva,item_id_item,usuario_id_usuario)
			values (date_add(now(), interval dias day),0,concat('PO',po),qtd,idItem,1);
    
end$$
delimiter ;

drop procedure if exists gerarreservaAleatorias;
delimiter $$
create procedure gerarreservaAleatorias()
begin
    declare qtd int;   
	set qtd = 10;
	while (qtd > -10) do
		call gerarReserva(qtd);       
		set qtd = qtd - 1;    
	end while;	
end$$
delimiter ;

drop procedure if exists gerarprevisaoAleatorias;
delimiter $$
create procedure gerarprevisaoAleatorias()
begin
    declare qtd int;   
	set qtd = 10;
	while (qtd > -10) do
		call gerarPrevisao(qtd);       
		set qtd = qtd - 1;    
	end while;	
end$$
delimiter ;

drop procedure if exists gerarMovimento;
delimiter $$
create procedure gerarMovimento(in idItem bigint, in tipo varchar(3), in qtd float, in org_dstn varchar(25), in dtMovimento datetime)
begin
	declare idEstoque bigint;  
    declare qtdReal float; 

	set idEstoque = (select e.id_estoque from estoque as e where e.item_id_item = idItem);
	set qtdReal = (select e.estoque_real from estoque as e where e.item_id_item = idItem);
	
	if (tipo = 'IN') then
		insert into movimentacao(data_movimentacao, origem_destino, quantidade, tipo, estoque_id_estoque, item_id_item, usuario_id_usuario)
					values(dtMovimento,org_dstn, qtd, tipo,idEstoque,idItem,1);
	
		update estoque set estoque_real = (qtdReal + qtd) where item_id_item = idItem;
	
	elseif(tipo = 'OUT') then
	
		if (qtdReal > qtd) then
			insert into movimentacao(data_movimentacao, origem_destino, quantidade, tipo, estoque_id_estoque, item_id_item, usuario_id_usuario)
					values(dtMovimento,org_dstn, qtd, tipo,idEstoque,idItem,1);
		
			update estoque set estoque_real = (qtdReal - qtd) where item_id_item = idItem;
		end if;
	end if;    
end$$
delimiter ;

drop procedure if exists gerarmovimentacaoUltimasreserva;
Delimiter $$
create procedure gerarmovimentacaoUltimasreserva(in num int)
begin
	
    declare idItem bigint;
    declare idReserva bigint;
    declare qtd float;
    declare vdia int;
    declare vParam int;
    declare destino varchar(25);
    declare nData datetime;

    set vdia = (round(rand() * (30 - 10 - 1)) + 1);
        
	while num > 0 do
		select ifnull(min(id_reserva),0) into idReserva from reserva where finalizada = false and data_prevista <= now(); 	-- busca a primeira previsao possível para entrada
        if(idReserva > 0) then        
			select quantidade_reserva into qtd from reserva where id_reserva = idReserva;
            select item_id_item into idItem from reserva where id_reserva = idReserva;
            select ordem into destino from reserva where id_reserva = idReserva;
            select data_prevista into ndata from reserva where id_reserva = idReserva;
			
            set vParam = datediff(now(),ndata);
            set ndata = date_add(now(),interval -vParam day);
            
			call gerarMovimento(idItem,'OUT',qtd,destino,nData);
            
            update reserva set finalizada = true where id_reserva = idReserva;
            
        end if;
        set num = num - 1;
    end while;       
end $$
Delimiter ;

drop procedure if exists gerarmovimentacaoUltimasprevisao;
Delimiter $$
create procedure gerarmovimentacaoUltimasprevisao(in num int)
begin
	
    declare idItem bigint;
    declare idPrevisao bigint;
    declare qtd float;
    declare vdia int;
    declare vParam int;
    declare origem varchar(25);
    declare nData datetime;
       
	while num > 0 do
		select ifnull(min(id_previsao),0) into idPrevisao from previsao where finalizada = false and data_prevista <= now(); 	-- busca a primeira previsao possível para entrada
        if(idPrevisao > 0) then        
			select quantidade_prevista into qtd from previsao where id_previsao = idPrevisao;
            select item_id_item into idItem from previsao where id_previsao = idPrevisao;
            select ordem into origem from previsao where id_previsao = idPrevisao;          
            select data_prevista into ndata from previsao where id_previsao = idPrevisao;
			
            set vParam = datediff(now(),ndata);
            set ndata = date_add(now(),interval -vParam day);
			set vdia = (round(rand() * (vParam - 2)) + 1);
            set ndata = date_add(ndata, interval vdia day);          
			call gerarMovimento(idItem,'IN',qtd,origem,nData);
            
            update previsao set finalizada = true where id_previsao = idPrevisao;
            
        end if;
        set num = num - 1;
    end while;       
end $$
Delimiter ;

drop procedure if exists gerarmovimentacaoAvulsas;
Delimiter $$
create procedure gerarmovimentacaoAvulsas(in qtdMovimentos int, in dias int)
begin	
    declare idInicio bigint;
    declare idFim bigint;
    declare idItem bigint;
    declare idEstoque bigint;
    declare qtd float;
    declare qtdMinima float;
    declare qtdReal float;
    declare vdia int;
    
    select max(id_item) into idFim from item;
    select min(id_item) into idInicio from item;
    
	while qtdMovimentos > 0 do
		select getIntAleatorio(idInicio,idFim,10) into idItem;
        
		set idEstoque = (select e.id_estoque from estoque as e where e.item_id_item = idItem);
		set qtdReal = (select e.estoque_real from estoque as e where e.item_id_item = idItem);
		set qtdMinima = (select i.estoque_seguranca from item as i where i.id_item = idItem);
		
		set qtd = getFloatAleatorio(1,qtdMinima);		
		   
		if(qtdReal > qtd) then
			
            set vdia = (round(rand() * (dias - 2)) + 1);
			call gerarMovimento(idItem,'OUT',qtd,'avulso',date_add(now(), interval vdia day));
            
		end if;
        
        set qtdMovimentos = qtdMovimentos - 1;
    end while;

end $$
Delimiter ;

call gerarprevisaoAleatorias();
call gerarreservaAleatorias();
call gerarmovimentacaoUltimasreserva(10);
call gerarmovimentacaoUltimasprevisao(10);
call gerarmovimentacaoAvulsas(10,5);


select * from -- movimentacao m
	-- inner join 
    item 
    -- on id_item= m.item_id_item
    inner join estoque e on id_item = e.item_id_item
    
where item.id_item = 1490
-- order by m.data_movimentacao
;

