# Relatório da 5ª Entrega de Engenharia de Software

# Introducão

A Fénix-Framework permite o desenvolvimento de aplicações Java que necessitam de um modelo de domínio transacional e persistente, como é o caso do nosso projeto. Neste relatório pretendemos avaliar em concreto o impacto que a sua política de concorrência tem na invocação de serviços remotos. Esta política é descrita como uma política optimista, pois em vez de assegurar a impossibilidade de acesso de dois processos ao mesmo valor, "assume" que esse acesso paralelo não irá acontecer. Este optimismo não tem implicações na performance em casos de leitura, mas pode causar graves atrasos em casos de escrita, como se pode ver na análise detalhada abaixo descrita. 

# 100 Writes
![stack Overflow](https://i.gyazo.com/a203e4fcfb77fd9dd01ff87fa3d57ea6.png)
![stack Overflow](https://i.gyazo.com/13036499de0f66e16b036fbd61bfed8e.png)
Neste caso, a política de escrita optimista do fénix framework é bastante notável. Isto porque estes 100 writes são de facto processamentos de estados de aventuras, pelo que implicam uma leitura e uma escrita. Assim, é frequente que, com a política optimista, estas operações interfiram entre si, causando uma repetição que aumenta a latência e diminui o throughput. Isto é realçado pela latência (em média) extremamente alta - 1640 ms - da configuração com mais paralelismo (100 utilizadores), comparada com a latência da configuração com apenas 1 utilizador - 33 ms. A melhor configuração registada, em termos de throughput, foi 5 utilizadores 20 ciclos, sendo que combina um pouco de paralelismo com uma sequencialidade considerável para evitar repetições. Vale a pena realçar que neste caso não houve qualquer tipo de erro (Error percentage de 0%), mas que com mais utilizadores a percentagem de erro seria bastante considerável.

# 30 Writes
![stack Overflow](https://i.gyazo.com/5442844d9d61cf506ced1d2537aeee7e.png)
![stack Overflow](https://i.gyazo.com/2542e0e043ab8d9ffc22cc8a0798f471.png)
Este input é constituído por 30% writes (Process Adventure) e 70% reads (Read Banks, por exemplo). Assim, este sofre do mesmo problema que o caso anterior, mas de uma forma diferente. Por outras palavras, este input faz com que os conflictos sejam ainda mais frequentes, pelo que temos writes e reads dos mesmos valores (Process Adventure e Read Adventures). Assim, temos uma latência mais elevada que acima com 100 utilizadores - 2834 ms. Contudo, sendo que as operações de leitura são mais eficientes, o Throughput não é tão baixo. Na melhor configuração (novamente 5 Utilizadores e 20 Ciclos), este ultrapassa os 25 requests/segundo, substancialmente mais alto que acima. Assim, vemos que a política optimista do fénix-framework é problemática com muitos utilizadores em paralelo, o que seria de esperar. A percentagem de erros é nula nestes testes, mas com 2000 utilizadores e 1 Ciclo esteve na ordem dos 19%, o que é espectável com este nível de concorrência.

# 100 Reads
![stack Overflow](https://i.gyazo.com/09636423c9b55531faa0d6db5a7aa854.png)
![stack Overflow](https://i.gyazo.com/e046e19fb9e193085db40d932f21f826.png)
O caso dos 100 Reads é bastante mais simples. Isto porque os reads não podem gerar conflictos, pois não alteram os valores aos quais acedem. Assim, a política de concorrência do fénx-framework funciona bastante bem, sendo que o optimismo nunca tem consequências negativas. Isto é demonstrado pelos dados recolhidos, que mostram que uma latência muito reduzida, ainda que substancial com vários utilizadores, devido a condições que não têm que ver com esta política (vários requests em simultâneo teram sempre maior delay devido ao funcionamento interno do sistema). O throughput, assim, duplica comparado com os anteriores, estando na ordem dos 65 requests/segundo na melhor configuração (10 utlizadores/10 Ciclos). Este input foi também testado com 2000 utilizadores, e obteve uma percentagem de erros pouco substancial: 0.621%.

# Conclusão
Concluindo, os resultados estiveram, acima de tudo, dentro do esperado tendo em conta o seu fundamento teórico. Estes 3 casos demonstram os pontos fortes - operações não sujeitas conflictos ou com pouco paralelismo - e os pontos fracos - operações sujeitas a conflictos com alto paralelismo - da política de concorrência do fénix-framework. Mais concretamente, a tarefa "100 Writes" teve bastante sucesso, até com bastante paralelismo, enquanto que as outras, que envolviam escritas, foram pouco eficientes quando aumentámos o número de utilizadores em simultâneo. 

# Referências
http://www.cs.unc.edu/~dewan/242/s06/notes/trans/node8.html
http://bnrg.cs.berkeley.edu/~adj/cs262/Lec_10_17b.html
https://fenix.tecnico.ulisboa.pt/downloadFile/845043405450348/The%20Fenix%20Framework%20Tutorial.pdf






















  
