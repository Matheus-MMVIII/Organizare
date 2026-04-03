const items = [
  { key: 'dashboard', label: 'Dashboard', description: 'Visao geral do sistema' },
  { key: 'users', label: 'Usuarios', description: 'Cadastrar e deletar usuarios' },
  { key: 'clothing', label: 'Roupas', description: 'Cadastrar e deletar produtos' },
  { key: 'buy', label: 'Compras', description: 'Registrar pedidos e acompanhar vendas' },
];

function SidebarNav({ currentRoute, navigate }) {
  return (
    <aside className="sidebar card">
      <div className="sidebar-brand">
        <span className="eyebrow">Studio</span>
        <h2>Organizare</h2>
        <p>Separado por paginas para deixar o fluxo mais claro.</p>
      </div>

      <nav className="sidebar-nav" aria-label="Navegacao principal">
        {items.map((item) => (
          <button
            key={item.key}
            className={`nav-link ${currentRoute === item.key ? 'active' : ''}`}
            type="button"
            onClick={() => navigate(item.key)}
          >
            <strong>{item.label}</strong>
            <span>{item.description}</span>
          </button>
        ))}
      </nav>
    </aside>
  );
}

export default SidebarNav;
