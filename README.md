# SimpleSQL

Essa Biblioteca tem como maior função facilitar o uso do SQLite para o android.

Agora vamos mostrar o passo a passo de como utilizar:
### Versões
v1.0.0

### Importando a lib para o projeto:
```GRADLLE
implementation 'com.github.sqlsimple.simplesql:{$last_version}'
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

```JAVA
public class HelperBD extends SQLiteOpenHelper {
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "example.db";
    Context context;

    public HelperBD(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) 
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
        onUpgrade(db, oldVersion, newVersion);
    }
}
```

### Exemplo de como inserir dados na sua Tabela

```JAVA
Pessoa p = new Pessoa();
p.setNome("Alow");
p.setIdade(12);
boolean result = false;
try {
	result = new SimpleSQL(new HelperBD(this)).insert(p);
} catch (Throwable throwable) {
	throwable.printStackTrace();
}
```

### Exemplo de como lista os dados da usa Tabela

```JAVA
SimpleSQL simpleSql = new SimpleSQL(new HelperBD(this));
List<Pessoa> lstPessoas = simpleSql.selectTable(new Pessoa())
		.where()
		.equals()
		.fields(new String[]{"*"})
		.execute();
```		

### Caso seja necessário atualizar o banco, faça da seguinte forma:

<blockquote>
  <p>
    Atualizar a versão do Banco
  </p>
</blockquote>

```JAVA
static final int DATABASE_VERSION = 2;
```

<blockquote>
  <p>
    Deletar sua tabela para ela possa receber ou remover algum dado
  </p>
</blockquote>

```JAVA
@Override
public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
	try {
		SimpleSQL.deleteTable(new Pessoa());
	} catch (SQLException e) {
		e.printStackTrace();
	}
	onCreate(sqLiteDatabase);
}
```

