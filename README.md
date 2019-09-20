# SimpleSQL

Essa Biblioteca tem como maior função facilitar o uso do SQLite para o android.

Agora vamos mostrar o passo a passo de como utilizar:
### Versões
<a href="">v1.0.0</a>  
<a href="">v1.0.1</a>  
<a href="">v1.0.2</a>  
<a href="">v1.0.3</a>  
<a href="">v1.0.4</a>
<a href="">v1.0.5</a>

### Importando a lib para o projeto:
```groovy
implementation 'com.github.p2jorg:simplesql:1.0.5'
```
##### *Observação - Caso você não tenha o JitPack, adicione em seu module project a linha de código com o comentário:
```groovy
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://jitpack.io" } // JitPack
        
    }
}
```
### Passo 1: Crie sua classe modelo
  Utilizando a biblioteca, a sua classe modelo também é a sua tabela de banco de dados,  
  basta você utilizar as anotações necessárias para que as duas se tornem uma só.
```JAVA
@Table
public class Pessoa {
    @Column(type = "INTEGER")
    @AutoIncrement()
    @Key
    private int id;
	
    @Column(type = "TEXT",
            non_null = true)
    private String nome;
	
    @Column(type = "INTEGER"
            ,non_null = true)
    private int idade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
}

```

### Passo 2: Criar uma classe herdando SQLiteOpenHelper
O Processo inicial de criar um banco de dados continua o mesmo, porém, como já foi visto anteriormente, a sua tabela já foi criada, então o que você precisar fazer é apenas chamar o um método da classe SimpleSQL dentro do método onCreate(SQLiteDatabase sqlLiteDatabase)

```JAVA
public class HelperBD extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "example.db";
    Context context;
    private SimpleSQL simpleSQL;

    public HelperBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
	simpleSQL = new SimpleSQL(this);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String _return = simpleSQL.create(new Pessoa(),db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String _return = simpleSQL.deleteTable(new Pessoa(),db);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }
}
```
### Deletar a tabela
 ```JAVA
@Override
public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
	String _return = simpleSQL.deleteTable(new Pessoa(),db);
	onCreate(sqLiteDatabase);
}
```
### INSERT
O método insert() irá retornar um valor booleano, onde true é quando for inserido com sucesso e false quando ocorrer algum erro
```JAVA
Pessoa p = new Pessoa();
p.setNome("Alow");
p.setIdade(12);
boolean result = false;
result = new SimpleSQL(new HelperBD(this)).insert(p);
```

### SELECT
Para fazer uma listagem dos registro do banco de dados é bem simples, é só criar ua instancia da classe SimpleSQL e utilizar o método selectTable(Objeto) e montar o select da forma que preferir.
```JAVA
SimpleSQL simpleSql = new SimpleSQL(new HelperBD(this));
 
List<Pessoa> list = simpleSQL.selectTable(new Pessoa())
			    .fields(new String[]{"*"})
			    .where()
			    .collumn("id")
			    .equals()
			    .fieldInt(1)
			    .execute();
 
```
### DELETE
Para remover algum registro da tabela, ainda segue o mesmo padrão dos métodos anteriores
```JAVA
boolean result = simpleSQL.deleteColumn(new Pessoa())
			.where()
			.column("id")
			.equals()
			.fieldInt(1)
			.execute();

```
### UPDATE
Ainda utilizando o mesmo padrão dos anteriores você também pode atualizar os registro do banco de dados, da seguinte forma:  
```JAVA
SimpleSQL simpleSql = new SimpleSQL(new HelperBD(this));
boolean result = simpleSQL.updateTable(new Pessoa())
                    	.set(new String[]{"nome","idade"})
                    	.values(new String[]{"Novo Nome","Nova Idade"})
                    	.where()
                    	.collumn("id")
                    	.equals()
                    	.fieldInt(1)
                    	.execute()
       
```
 
# Desenvolvedores
<a href="https://github.com/PauloYR">Paulo Iury<a>  
<a href="https://github.com/LukNasc">Lucas Nascimento<a>  
<a href="https://github.com/jisellevms">Jiselle Martins<a>  
