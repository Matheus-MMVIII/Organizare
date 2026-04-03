import BuyForm from '../components/BuyForm';
import BuyList from '../components/BuyList';
import SectionIntro from '../components/SectionIntro';

function BuyPage({ app }) {
  return (
    <section className="page-content">
      <section className="page-header card">
        <SectionIntro
          eyebrow="Compras"
          title="Gerenciar compras"
          description="Registre novas compras, acompanhe os pedidos feitos e mantenha o estoque sincronizado."
          actions={
            <label className="search-box compact-search" htmlFor="buy-search">
              <span>Buscar compra</span>
              <input
                id="buy-search"
                type="text"
                placeholder="Usuario, roupa, data ou valor"
                value={app.buySearch}
                onChange={(event) => app.setBuySearch(event.target.value)}
              />
            </label>
          }
        />
      </section>

      <section className="workspace-grid page-grid">
        <BuyForm
          form={app.buyForm}
          users={app.users}
          clothing={app.clothing}
          onChange={app.updateBuyField}
          onSubmit={app.createBuy}
          submitting={app.submitting.buy}
        />

        <article className="card section-card list-card">
          <div className="section-header compact-header sticky-header">
            <div>
              <span className="eyebrow">Lista</span>
              <h3>Compras registradas</h3>
            </div>
            <p>{app.filteredBuys.length} resultado(s)</p>
          </div>

          <BuyList
            buys={app.filteredBuys}
            loading={app.loading}
            deletingId={app.deletingId}
            onDelete={app.deleteBuy}
          />
        </article>
      </section>
    </section>
  );
}

export default BuyPage;
