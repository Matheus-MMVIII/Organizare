import { useEffect, useMemo, useState } from 'react';

import SectionIntro from '../components/SectionIntro';
import StatCard from '../components/StatCard';

const CHART_WINDOW_SIZE = 7;
const LINE_CHART_HEIGHT = 145;
const CHART_PADDING = { top: 22, right: 22, bottom: 32, left: 22 };

const currencyFormatter = new Intl.NumberFormat('pt-BR', {
  style: 'currency',
  currency: 'BRL',
});

function capitalize(text) {
  if (!text) {
    return '';
  }

  return text.charAt(0).toUpperCase() + text.slice(1);
}

function formatCurrency(value) {
  return currencyFormatter.format(Number(value || 0));
}

function buildUsersMonthChart(users = []) {
  const safeUsers = Array.isArray(users) ? users : [];
  const today = new Date();
  const currentYear = today.getFullYear();
  const currentMonth = today.getMonth();
  const currentDay = today.getDate();
  const monthLabel = capitalize(
    new Intl.DateTimeFormat('pt-BR', {
      month: 'long',
      year: 'numeric',
    }).format(today),
  );

  const points = Array.from({ length: currentDay }, (_, index) => ({
    day: index + 1,
    count: 0,
  }));

  safeUsers.forEach((user) => {
    const createdAt = new Date(user.createdAt ?? '');
    if (Number.isNaN(createdAt.getTime())) {
      return;
    }

    if (createdAt.getFullYear() === currentYear && createdAt.getMonth() === currentMonth) {
      const dayIndex = createdAt.getDate() - 1;
      if (points[dayIndex]) {
        points[dayIndex].count += 1;
      }
    }
  });

  const total = points.reduce((sum, point) => sum + point.count, 0);
  const peakCount = points.reduce((highest, point) => Math.max(highest, point.count), 0);
  const peakDay = points.find((point) => point.count === peakCount)?.day ?? null;

  return {
    monthLabel,
    total,
    peakCount,
    peakDay,
    points,
  };
}

function buildBuysMonthChart(buys = []) {
  const safeBuys = Array.isArray(buys) ? buys : [];
  const today = new Date();
  const currentYear = today.getFullYear();
  const currentMonth = today.getMonth();
  const currentDay = today.getDate();
  const monthLabel = capitalize(
    new Intl.DateTimeFormat('pt-BR', {
      month: 'long',
      year: 'numeric',
    }).format(today),
  );

  const points = Array.from({ length: currentDay }, (_, index) => ({
    day: index + 1,
    count: 0,
    revenue: 0,
  }));

  safeBuys.forEach((buy) => {
    const orderDate = new Date(buy.orderDate ?? '');
    if (Number.isNaN(orderDate.getTime())) {
      return;
    }

    if (orderDate.getFullYear() === currentYear && orderDate.getMonth() === currentMonth) {
      const dayIndex = orderDate.getDate() - 1;
      if (points[dayIndex]) {
        points[dayIndex].count += 1;
        points[dayIndex].revenue += Number(buy.totalPrice || 0);
      }
    }
  });

  const totalOrders = points.reduce((sum, point) => sum + point.count, 0);
  const totalRevenue = points.reduce((sum, point) => sum + point.revenue, 0);
  const peakCount = points.reduce((highest, point) => Math.max(highest, point.count), 0);
  const peakDay = points.find((point) => point.count === peakCount)?.day ?? null;

  return {
    monthLabel,
    totalOrders,
    totalRevenue,
    peakCount,
    peakDay,
    points,
  };
}

function buildLineChartGeometry(points) {
  const svgWidth = Math.max(360, points.length * 88);
  const chartWidth = svgWidth - CHART_PADDING.left - CHART_PADDING.right;
  const chartHeight = LINE_CHART_HEIGHT - CHART_PADDING.top - CHART_PADDING.bottom;
  const maxCount = points.reduce((highest, point) => Math.max(highest, point.count), 0);
  const baseY = CHART_PADDING.top + chartHeight;
  const restingY = CHART_PADDING.top + chartHeight * 0.78;

  const getX = (index) => {
    if (points.length <= 1) {
      return CHART_PADDING.left + chartWidth / 2;
    }

    return CHART_PADDING.left + (index / (points.length - 1)) * chartWidth;
  };

  const getY = (count) => {
    if (maxCount === 0) {
      return restingY;
    }

    return CHART_PADDING.top + chartHeight - (count / maxCount) * chartHeight;
  };

  const chartPoints = points.map((point, index) => ({
    ...point,
    x: getX(index),
    y: getY(point.count),
  }));

  const linePath = chartPoints
    .map((point, index) => `${index === 0 ? 'M' : 'L'} ${point.x} ${point.y}`)
    .join(' ');

  const areaPath =
    chartPoints.length > 0
      ? `${linePath} L ${chartPoints.at(-1).x} ${baseY} L ${chartPoints[0].x} ${baseY} Z`
      : '';

  const gridLines = [0, 0.25, 0.5, 0.75, 1].map((ratio) => ({
    y: CHART_PADDING.top + chartHeight * ratio,
  }));

  return {
    svgWidth,
    svgHeight: LINE_CHART_HEIGHT,
    chartPoints,
    linePath,
    areaPath,
    gridLines,
    baseY,
  };
}

function DashboardPage({ app, navigate }) {
  const users = Array.isArray(app.users) ? app.users : [];
  const clothing = Array.isArray(app.clothing) ? app.clothing : [];
  const buys = Array.isArray(app.buys) ? app.buys : [];
  const recentUsers = Array.isArray(app.recentUsers) ? app.recentUsers : [];
  const recentClothing = Array.isArray(app.recentClothing) ? app.recentClothing : [];
  const recentBuys = Array.isArray(app.recentBuys) ? app.recentBuys : [];

  const usersMonthChart = useMemo(() => buildUsersMonthChart(users), [users]);
  const buysMonthChart = useMemo(() => buildBuysMonthChart(buys), [buys]);

  const usersTotalWindows = Math.max(1, Math.ceil(usersMonthChart.points.length / CHART_WINDOW_SIZE));
  const buysTotalWindows = Math.max(1, Math.ceil(buysMonthChart.points.length / CHART_WINDOW_SIZE));

  const [usersChartWindowIndex, setUsersChartWindowIndex] = useState(usersTotalWindows - 1);
  const [buysChartWindowIndex, setBuysChartWindowIndex] = useState(buysTotalWindows - 1);

  useEffect(() => {
    setUsersChartWindowIndex(usersTotalWindows - 1);
  }, [usersTotalWindows]);

  useEffect(() => {
    setBuysChartWindowIndex(buysTotalWindows - 1);
  }, [buysTotalWindows]);

  const usersStartIndex = usersChartWindowIndex * CHART_WINDOW_SIZE;
  const visibleUserPoints = usersMonthChart.points.slice(usersStartIndex, usersStartIndex + CHART_WINDOW_SIZE);
  const visibleUsersTotal = visibleUserPoints.reduce((sum, point) => sum + point.count, 0);
  const visibleUsersPeak = visibleUserPoints.reduce((highest, point) => Math.max(highest, point.count), 0);
  const visibleUsersRangeStart = visibleUserPoints[0]?.day ?? 1;
  const visibleUsersRangeEnd = visibleUserPoints.at(-1)?.day ?? visibleUsersRangeStart;
  const isViewingLatestUsersWindow = usersChartWindowIndex === usersTotalWindows - 1;
  const usersChartGeometry = useMemo(() => buildLineChartGeometry(visibleUserPoints), [visibleUserPoints]);
  const latestUserPoint = usersChartGeometry.chartPoints.at(-1);

  const buysStartIndex = buysChartWindowIndex * CHART_WINDOW_SIZE;
  const visibleBuyPoints = buysMonthChart.points.slice(buysStartIndex, buysStartIndex + CHART_WINDOW_SIZE);
  const visibleBuysTotal = visibleBuyPoints.reduce((sum, point) => sum + point.count, 0);
  const visibleBuysRevenue = visibleBuyPoints.reduce((sum, point) => sum + point.revenue, 0);
  const visibleBuysPeak = visibleBuyPoints.reduce((highest, point) => Math.max(highest, point.count), 0);
  const visibleBuysRangeStart = visibleBuyPoints[0]?.day ?? 1;
  const visibleBuysRangeEnd = visibleBuyPoints.at(-1)?.day ?? visibleBuysRangeStart;
  const isViewingLatestBuysWindow = buysChartWindowIndex === buysTotalWindows - 1;
  const buysChartGeometry = useMemo(() => buildLineChartGeometry(visibleBuyPoints), [visibleBuyPoints]);
  const latestBuyPoint = buysChartGeometry.chartPoints.at(-1);

  return (
    <section className="page-content">
      <section className="dashboard-analytics">
        <article className="card section-card chart-card">
          <SectionIntro
            eyebrow="Crescimento"
            title="Novos usuarios no mes"
            description={`Linha de cadastros em ${usersMonthChart.monthLabel}, com foco no periodo mais recente.`}
            actions={
              <div className="chart-period-actions">
                <span className={`chart-window-label${isViewingLatestUsersWindow ? ' is-current' : ''}`}>
                  Dias {String(visibleUsersRangeStart).padStart(2, '0')} a{' '}
                  {String(visibleUsersRangeEnd).padStart(2, '0')}
                </span>
                <button
                  className="secondary-button chart-nav-button"
                  type="button"
                  onClick={() => setUsersChartWindowIndex((current) => Math.max(0, current - 1))}
                  disabled={usersChartWindowIndex === 0}
                >
                  Anteriores
                </button>
                <button
                  className="secondary-button chart-nav-button"
                  type="button"
                  onClick={() =>
                    setUsersChartWindowIndex((current) => Math.min(usersTotalWindows - 1, current + 1))
                  }
                  disabled={isViewingLatestUsersWindow}
                >
                  Mais recente
                </button>
              </div>
            }
          />

          <div className="chart-summary-row">
            <div className="chart-highlight">
              <strong>{usersMonthChart.total}</strong>
              <span>cadastro(s) neste mes</span>
            </div>
            <p>
              {usersMonthChart.peakCount > 0
                ? `Pico geral no dia ${usersMonthChart.peakDay}, com ${usersMonthChart.peakCount} cadastro(s). Nesta janela, o maior volume foi ${visibleUsersPeak}.`
                : 'Assim que surgirem novos usuarios neste mes, a linha aparecera aqui com o periodo mais recente em destaque.'}
            </p>
          </div>

          <div className="chart-focus-row">
            <span className="chart-focus-badge">{isViewingLatestUsersWindow ? 'Foco atual' : 'Periodo anterior'}</span>
            <strong>{visibleUsersTotal} cadastro(s) neste recorte visivel</strong>
            <span>{usersTotalWindows > 1 ? `Janela ${usersChartWindowIndex + 1} de ${usersTotalWindows}` : 'Mes atual completo'}</span>
          </div>

          <div className="chart-scroll">
            <div className="line-chart-shell">
              <svg
                className="line-chart-svg"
                viewBox={`0 0 ${usersChartGeometry.svgWidth} ${usersChartGeometry.svgHeight}`}
                role="img"
                aria-label="Grafico de linha de novos usuarios por dia no mes atual"
              >
                <defs>
                  <linearGradient id="users-line-gradient" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stopColor="rgba(221, 107, 66, 0.95)" />
                    <stop offset="100%" stopColor="rgba(18, 91, 80, 0.98)" />
                  </linearGradient>
                  <linearGradient id="users-area-gradient" x1="0%" y1="0%" x2="0%" y2="100%">
                    <stop offset="0%" stopColor="rgba(18, 91, 80, 0.22)" />
                    <stop offset="100%" stopColor="rgba(18, 91, 80, 0.02)" />
                  </linearGradient>
                </defs>

                {usersChartGeometry.gridLines.map((gridLine) => (
                  <line
                    key={gridLine.y}
                    className="chart-grid-line"
                    x1={CHART_PADDING.left}
                    x2={usersChartGeometry.svgWidth - CHART_PADDING.right}
                    y1={gridLine.y}
                    y2={gridLine.y}
                  />
                ))}

                {usersChartGeometry.areaPath ? <path className="chart-area" d={usersChartGeometry.areaPath} /> : null}
                {usersChartGeometry.linePath ? <path className="chart-line" d={usersChartGeometry.linePath} /> : null}

                {usersChartGeometry.chartPoints.map((point) => (
                  <g key={point.day}>
                    <circle
                      className={`chart-dot${latestUserPoint?.day === point.day ? ' chart-dot-active' : ''}`}
                      cx={point.x}
                      cy={point.y}
                      r={latestUserPoint?.day === point.day ? 6 : 4.5}
                    />
                    <text className="chart-point-value" x={point.x} y={point.y - 12} textAnchor="middle">
                      {point.count}
                    </text>
                    <text className="chart-axis-text" x={point.x} y={usersChartGeometry.baseY + 18} textAnchor="middle">
                      {String(point.day).padStart(2, '0')}
                    </text>
                  </g>
                ))}
              </svg>
            </div>
          </div>
        </article>

        <article className="card section-card chart-card">
          <SectionIntro
            eyebrow="Compras"
            title="Movimento de compras no mes"
            description={`Pedidos registrados em ${buysMonthChart.monthLabel}, com foco no recorte mais recente.`}
            actions={
              <div className="chart-period-actions">
                <span className={`chart-window-label${isViewingLatestBuysWindow ? ' is-current' : ''}`}>
                  Dias {String(visibleBuysRangeStart).padStart(2, '0')} a{' '}
                  {String(visibleBuysRangeEnd).padStart(2, '0')}
                </span>
                <button
                  className="secondary-button chart-nav-button"
                  type="button"
                  onClick={() => setBuysChartWindowIndex((current) => Math.max(0, current - 1))}
                  disabled={buysChartWindowIndex === 0}
                >
                  Anteriores
                </button>
                <button
                  className="secondary-button chart-nav-button"
                  type="button"
                  onClick={() => setBuysChartWindowIndex((current) => Math.min(buysTotalWindows - 1, current + 1))}
                  disabled={isViewingLatestBuysWindow}
                >
                  Mais recente
                </button>
                <button className="primary-button alt-button" type="button" onClick={() => navigate('buy')}>
                  Ver compras
                </button>
              </div>
            }
          />

          <div className="chart-summary-row">
            <div className="chart-highlight">
              <strong>{formatCurrency(buysMonthChart.totalRevenue)}</strong>
              <span>faturamento neste mes</span>
            </div>
            <p>
              {buysMonthChart.peakCount > 0
                ? `${buysMonthChart.totalOrders} compra(s) registradas no mes. O pico de pedidos foi no dia ${buysMonthChart.peakDay}, com ${buysMonthChart.peakCount} compra(s).`
                : 'Assim que as compras forem registradas, este grafico passa a mostrar o ritmo de pedidos do mes atual.'}
            </p>
          </div>

          <div className="chart-focus-row">
            <span className="chart-focus-badge">{isViewingLatestBuysWindow ? 'Foco atual' : 'Periodo anterior'}</span>
            <strong>{visibleBuysTotal} compra(s) neste recorte visivel</strong>
            <span>{formatCurrency(visibleBuysRevenue)} no mesmo periodo</span>
            <span>{visibleBuysPeak > 0 ? `Pico visivel de ${visibleBuysPeak} pedido(s)` : 'Sem pedidos nesta janela'}</span>
          </div>

          <div className="chart-scroll">
            <div className="line-chart-shell">
              <svg
                className="line-chart-svg"
                viewBox={`0 0 ${buysChartGeometry.svgWidth} ${buysChartGeometry.svgHeight}`}
                role="img"
                aria-label="Grafico de linha de compras por dia no mes atual"
              >
                <defs>
                  <linearGradient id="sales-line-gradient" x1="0%" y1="0%" x2="100%" y2="0%">
                    <stop offset="0%" stopColor="rgba(18, 91, 80, 0.98)" />
                    <stop offset="100%" stopColor="rgba(221, 107, 66, 0.95)" />
                  </linearGradient>
                  <linearGradient id="sales-area-gradient" x1="0%" y1="0%" x2="0%" y2="100%">
                    <stop offset="0%" stopColor="rgba(221, 107, 66, 0.2)" />
                    <stop offset="100%" stopColor="rgba(221, 107, 66, 0.03)" />
                  </linearGradient>
                </defs>

                {buysChartGeometry.gridLines.map((gridLine) => (
                  <line
                    key={gridLine.y}
                    className="chart-grid-line"
                    x1={CHART_PADDING.left}
                    x2={buysChartGeometry.svgWidth - CHART_PADDING.right}
                    y1={gridLine.y}
                    y2={gridLine.y}
                  />
                ))}

                {buysChartGeometry.areaPath ? (
                  <path className="chart-area" d={buysChartGeometry.areaPath} style={{ fill: 'url(#sales-area-gradient)' }} />
                ) : null}
                {buysChartGeometry.linePath ? (
                  <path className="chart-line" d={buysChartGeometry.linePath} style={{ stroke: 'url(#sales-line-gradient)' }} />
                ) : null}

                {buysChartGeometry.chartPoints.map((point) => (
                  <g key={point.day}>
                    <circle
                      className={`chart-dot${latestBuyPoint?.day === point.day ? ' chart-dot-active' : ''}`}
                      cx={point.x}
                      cy={point.y}
                      r={latestBuyPoint?.day === point.day ? 6 : 4.5}
                      style={latestBuyPoint?.day === point.day ? { stroke: 'var(--accent)' } : undefined}
                    />
                    <text className="chart-point-value" x={point.x} y={point.y - 12} textAnchor="middle">
                      {point.count}
                    </text>
                    <text className="chart-axis-text" x={point.x} y={buysChartGeometry.baseY + 18} textAnchor="middle">
                      {String(point.day).padStart(2, '0')}
                    </text>
                  </g>
                ))}
              </svg>
            </div>
          </div>
        </article>
      </section>

      <section className="stats-grid">
        <StatCard label="Usuarios cadastrados" value={users.length} detail="Total salvo na base" />
        <StatCard label="Roupas cadastradas" value={clothing.length} detail="Itens no catalogo" tone="accent-card" />
        <StatCard label="Compras registradas" value={buys.length} detail="Pedidos feitos ate agora" />
        <StatCard
          label="Faturamento do mes"
          value={formatCurrency(app.currentMonthRevenue)}
          detail="Soma das compras do mes atual"
          tone="warm-card"
        />
      </section>

      <section className="workspace-grid dashboard-grid">
        <article className="card section-card">
          <SectionIntro
            eyebrow="Usuarios"
            title="Ultimos usuarios"
            description="Uma visao rapida antes de abrir a pagina completa de gerenciamento."
          />

          <div className="mini-list">
            {app.loading ? <p className="empty-state">Carregando usuarios...</p> : null}
            {!app.loading && recentUsers.length === 0 ? <p className="empty-state">Nenhum usuario cadastrado.</p> : null}
            {recentUsers.map((user) => (
              <article key={user.id} className="mini-card">
                <strong>{user.name}</strong>
                <span>{user.email}</span>
              </article>
            ))}
          </div>
        </article>

        <article className="card section-card">
          <SectionIntro
            eyebrow="Roupas"
            title="Ultimos produtos"
            description="Acompanhe rapidamente as ultimas pecas adicionadas ao catalogo."
          />

          <div className="mini-list">
            {app.loading ? <p className="empty-state">Carregando roupas...</p> : null}
            {!app.loading && recentClothing.length === 0 ? <p className="empty-state">Nenhuma roupa cadastrada.</p> : null}
            {recentClothing.map((item) => (
              <article key={item.id} className="mini-card">
                <strong>{item.name}</strong>
                <span>
                  {item.color} • Tam. {item.size} • Estoque {item.stock}
                </span>
              </article>
            ))}
          </div>
        </article>

        <article className="card section-card">
          <SectionIntro
            eyebrow="Compras"
            title="Ultimas compras"
            description="Pedidos mais recentes para bater o olho antes de abrir a pagina dedicada."
          />

          <div className="mini-list">
            {app.loading ? <p className="empty-state">Carregando compras...</p> : null}
            {!app.loading && recentBuys.length === 0 ? <p className="empty-state">Nenhuma compra registrada.</p> : null}
            {recentBuys.map((buy) => (
              <article key={buy.id} className="mini-card">
                <strong>{formatCurrency(buy.totalPrice)}</strong>
                <span>
                  {buy.userName} • {buy.clothingName} • {buy.quantity} unidade(s)
                </span>
              </article>
            ))}
          </div>
        </article>
      </section>
    </section>
  );
}

export default DashboardPage;
