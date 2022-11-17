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
    
    select max(id_item) into idFim from itens;
    select min(id_item) into idInicio from itens;
    
    set compras = (select COUNT(*) from previsoes) + 1000;
        
	set qtd = getFloatAleatorio(50,100);    
    select getIntAleatorio(idInicio,idFim,10) into idItem;
    
    insert into previsoes (data_prevista,finalizada,ordem,quantidade_prevista,item_id_item,usuario_id_usuario)
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
    
    select max(id_item) into idFim from itens;
    select min(id_item) into idInicio from itens;
    
    set po = (select COUNT(*) from reservas) + 1000;
        
	set qtd = getFloatAleatorio(50,100);    
    select getIntAleatorio(idInicio,idFim,10) into idItem;
    
    insert into reservas (data_prevista,finalizada,ordem,quantidade_reserva,item_id_item,usuario_id_usuario)
			values (date_add(now(), interval dias day),0,concat('PO',po),qtd,idItem,1);
    
end$$
delimiter ;

drop procedure if exists gerarReservasAleatorias;
delimiter $$
create procedure gerarReservasAleatorias()
begin
    declare qtd int;   
	set qtd = 10;
	while (qtd > -10) do
		call gerarReserva(qtd);       
		set qtd = qtd - 1;    
	end while;	
end$$
delimiter ;

drop procedure if exists gerarPrevisoesAleatorias;
delimiter $$
create procedure gerarPrevisoesAleatorias()
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
		insert into movimentacoes(data_movimentacao, origem_destino, quantidade, tipo, estoque_id_estoque, item_id_item, usuario_id_usuario)
					values(dtMovimento,org_dstn, qtd, tipo,idEstoque,idItem,1);
	
		update estoque set estoque_real = (qtdReal + qtd) where item_id_item = idItem;
	
	elseif(tipo = 'OUT') then
	
		if (qtdReal > qtd) then
			insert into movimentacoes(data_movimentacao, origem_destino, quantidade, tipo, estoque_id_estoque, item_id_item, usuario_id_usuario)
					values(dtMovimento,org_dstn, qtd, tipo,idEstoque,idItem,1);
		
			update estoque set estoque_real = (qtdReal - qtd) where item_id_item = idItem;
		end if;
	end if;    
end$$
delimiter ;

drop procedure if exists gerarMovimentacoesUltimasReservas;
Delimiter $$
create procedure gerarMovimentacoesUltimasReservas(in num int)
begin
	
    declare idItem bigint;
    declare idReserva bigint;
    declare qtd float;
    declare vdia int;
    declare destino varchar(25);
    declare nData datetime;

    set vdia = (round(rand() * (30 - 10 - 1)) + 1);
        
	while num > 0 do
		select ifnull(min(id_reserva),0) into idReserva from reservas where finalizada = false and data_prevista <= now(); 	-- busca a primeira previsao possível para entrada
        if(idReserva > 0) then        
			select quantidade_reserva into qtd from reservas where id_reserva = idReserva;
            select item_id_item into idItem from reservas where id_reserva = idReserva;
            select ordem into destino from reservas where id_reserva = idReserva;
            select data_prevista into ndata from reservas where id_reserva = idReserva;            
			call gerarMovimento(idItem,'OUT',qtd,destino,nData);
            
            update reservas set finalizada = true where id_reserva = idReserva;
            
        end if;
        set num = num - 1;
    end while;       
end $$
Delimiter ;

drop procedure if exists gerarMovimentacoesUltimasPrevisoes;
Delimiter $$
create procedure gerarMovimentacoesUltimasPrevisoes(in num int)
begin
	
    declare idItem bigint;
    declare idPrevisao bigint;
    declare qtd float;
    declare vdia int;
    declare vParam int;
    declare origem varchar(25);
    declare nData datetime;
       
	while num > 0 do
		select ifnull(min(id_previsao),0) into idPrevisao from previsoes where finalizada = false and data_prevista <= now(); 	-- busca a primeira previsao possível para entrada
        if(idPrevisao > 0) then        
			select quantidade_prevista into qtd from previsoes where id_previsao = idPrevisao;
            select item_id_item into idItem from previsoes where id_previsao = idPrevisao;
            select ordem into origem from previsoes where id_previsao = idPrevisao;          
            select data_prevista into ndata from previsoes where id_previsao = idPrevisao;
			
            set vParam = datediff(now(),ndata); 
			set vdia = (round(rand() * (vParam - 2)) + 1);
            set ndata = date_add(ndata, interval vdia day);          
			call gerarMovimento(idItem,'IN',qtd,origem,nData);
            
            update previsoes set finalizada = true where id_previsao = idPrevisao;
            
        end if;
        set num = num - 1;
    end while;       
end $$
Delimiter ;

drop procedure if exists gerarMovimentacoesAvulsas;
Delimiter $$
create procedure gerarMovimentacoesAvulsas(in qtdMovimentos int, in dias int)
begin	
    declare idInicio bigint;
    declare idFim bigint;
    declare idItem bigint;
    declare idEstoque bigint;
    declare qtd float;
    declare qtdMinima float;
    declare qtdReal float;
    declare vdia int;
    
    select max(id_item) into idFim from itens;
    select min(id_item) into idInicio from itens;
    
	while qtdMovimentos > 0 do
		select getIntAleatorio(idInicio,idFim,10) into idItem;	
		set idEstoque = (select e.id_estoque from estoque as e where e.item_id_item = idItem);
		set qtdReal = (select e.estoque_real from estoque as e where e.item_id_item = idItem);
		set qtdMinima = (select i.estoque_seguranca from itens as i where i.id_item = idItem);
		
		set qtd = getFloatAleatorio(1,qtdMinima);		
		   
		if(qtdReal > qtd) then
			
			call gerarMovimento(idItem,'OUT',qtd,'avulso',date_add(now(), interval dias day));
            
		end if;
        
        set qtdMovimentos = qtdMovimentos - 1;
    end while;

end $$
Delimiter ;

call gerarPrevisoesAleatorias();
call gerarReservasAleatorias();
call gerarMovimentacoesUltimasReservas(10);
call gerarMovimentacoesUltimasPrevisoes(10);
call gerarMovimentacoesAvulsas(10,5);


select * from movimentacoes m
	inner join itens on id_item= m.item_id_item
    inner join estoque e on id_item = e.item_id_item
    order by m.data_movimentacao
;