package com.example.eduardo94251

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.eduardo94251.viewmodel.ItemsAdapter
import com.example.eduardo94251.viewmodel.ItemsViewModel
import com.example.eduardo94251.viewmodel.ItemsViewModelFactory


class MainActivity : AppCompatActivity() {

    // O ViewModel usado para interagir com o banco de dados.
    private lateinit var viewModel: ItemsViewModel

    /**
     * Chamado quando a activity é criada.
     * Este método configura a interface do usuário e inicializa o ViewModel.
     *
     * @param savedInstanceState Se a activity está sendo recriada a partir de um estado salvo, este é o estado.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // Chama o método onCreate da superclasse para completar a criação da activity.
        super.onCreate(savedInstanceState)
        // Define o layout da activity.
        setContentView(R.layout.activity_main)

        // Encontra a barra de ferramentas pelo seu ID e a define como a barra de ação para esta activity.
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Define o título da barra de ação.
        supportActionBar?.title = "EcoDicas"

        // Encontra o RecyclerView pelo seu ID.
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Cria um novo adaptador para o RecyclerView. O adaptador é responsável por exibir os itens na lista.
        // Quando um item é clicado, ele é removido da lista.
        val itemsAdapter = ItemsAdapter { item ->
            viewModel.removeItem(item)
        }
        // Define o adaptador do RecyclerView.
        recyclerView.adapter = itemsAdapter

        // Encontra o botão e o campo de texto pelo seus IDs.
        val button = findViewById<Button>(R.id.button)
        val editText = findViewById<EditText>(R.id.editText)

        // Define o que acontece quando o botão é clicado.
        button.setOnClickListener {
            // Se o campo de texto estiver vazio, exibe um erro e retorna.
            if (editText.text.isEmpty()) {
                editText.error = "Preencha um valor"
                return@setOnClickListener
            }

            // Adiciona o item ao ViewModel e limpa o campo de texto.
            viewModel.addItem(editText.text.toString())
            editText.text.clear()
        }

        // Cria uma nova fábrica para o ViewModel.
        val viewModelFactory = ItemsViewModelFactory(application)
        // Obtém uma instância do ViewModel.
        viewModel = ViewModelProvider(this, viewModelFactory).get(ItemsViewModel::class.java)

        // Observa as mudanças na lista de itens e atualiza o adaptador quando a lista muda.
        viewModel.itemsLiveData.observe(this) { items ->
            itemsAdapter.updateItems(items)
        }
    }
}