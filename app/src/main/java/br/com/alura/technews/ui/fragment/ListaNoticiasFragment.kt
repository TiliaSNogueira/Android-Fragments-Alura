package br.com.alura.technews.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.alura.technews.R
import br.com.alura.technews.model.Noticia
import br.com.alura.technews.ui.fragment.extensions.mostraErro
import br.com.alura.technews.ui.recyclerview.adapter.ListaNoticiasAdapter
import br.com.alura.technews.ui.viewmodel.ListaNoticiasViewModel
import kotlinx.android.synthetic.main.lista_noticias.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.IllegalArgumentException

class ListaNoticiasFragment : Fragment() {

    private val MENSAGEM_FALHA_CARREGAR_NOTICIAS = "Não foi possível carregar as novas notícias"

    private val adapter by lazy {
        context?.let {
            ListaNoticiasAdapter(context = it)
        } ?: throw IllegalArgumentException("Contexto inválido")
    }
    private val viewModel: ListaNoticiasViewModel by viewModel()

  //  private lateinit var listener: ListaNoticiasListener
    var quandoFabSalvaNoticiaClicado : () -> Unit = {}
    var quandoNoticiaSelecionada : (noticia: Noticia) -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buscaNoticias()
        //para ter acesso aos métodos da ListaNoticiasActivity
       // listener = activity as ListaNoticiasListener

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lista_noticias, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
        configuraFabAdicionaNoticia()
    }

    private fun configuraFabAdicionaNoticia() {
        lista_noticias_fab_salva_noticia.setOnClickListener {
            quandoFabSalvaNoticiaClicado()
        }
    }

    private fun configuraRecyclerView() {
        val divisor = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        lista_noticias_recyclerview.addItemDecoration(divisor)
        lista_noticias_recyclerview.adapter = adapter
        configuraAdapter()
    }

    private fun configuraAdapter() {
        //o contexto é a ListaNoticiasActivity

        adapter.quandoItemClicado = quandoNoticiaSelecionada
    }

    private fun buscaNoticias() {
        viewModel.buscaTodos().observe(this, Observer { resource ->
            resource.dado?.let { adapter.atualiza(it) }
            resource.erro?.let {
                mostraErro(MENSAGEM_FALHA_CARREGAR_NOTICIAS)
            }
        })
    }

//    interface ListaNoticiasListener {
//        fun quandoNoticiaSelecionada(noticia: Noticia)
//        fun quandoFabSalvaNoticiaClicado()
//    }
}