import ClothingForm from '../components/ClothingForm';
import ClothingList from '../components/ClothingList';
import SectionIntro from '../components/SectionIntro';

function ClothingPage({ app }) {
  return (
    <section className="page-content">
      <section className="page-header card">
        <SectionIntro
          eyebrow="Roupas"
          title="Gerenciar roupas"
          description="Pagina separada para cadastrar produtos e deletar pecas do catalogo com mais clareza."
          actions={
            <label className="search-box compact-search" htmlFor="clothing-search">
              <span>Buscar roupa</span>
              <input
                id="clothing-search"
                type="text"
                placeholder="Nome, cor ou tamanho"
                value={app.clothingSearch}
                onChange={(event) => app.setClothingSearch(event.target.value)}
              />
            </label>
          }
        />
      </section>

      <section className="workspace-grid page-grid">
        <ClothingForm
          form={app.clothingForm}
          onChange={app.updateClothingField}
          onSubmit={app.createClothing}
          submitting={app.submitting.clothing}
        />

        <article className="card section-card list-card">
          <div className="section-header compact-header sticky-header">
            <div>
              <span className="eyebrow">Lista</span>
              <h3>Roupas cadastradas</h3>
            </div>
            <p>{app.filteredClothing.length} resultado(s)</p>
          </div>

          <ClothingList
            clothing={app.filteredClothing}
            loading={app.loading}
            deletingId={app.deletingId}
            onDelete={app.deleteClothing}
          />
        </article>
      </section>
    </section>
  );
}

export default ClothingPage;
