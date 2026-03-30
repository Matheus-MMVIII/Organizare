import { useEffect, useMemo, useState } from 'react';

import SectionIntro from '../components/SectionIntro';
import StatCard from '../components/StatCard';

const CHART_WINDOW_SIZE = 7;
const LINE_CHART_HEIGHT = 145;
const CHART_PADDING = { top: 22, right: 22, bottom: 32, left: 22 };

function capitalize(text) {
  if (!text) {
    return '';
  }

  return text.charAt(0).toUpperCase() + text.slice(1);
}

function buildUsersMonthChart(users) {
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

  users.forEach((user) => {
    const createdAt = new Date(user.createdAt ?? '');
    if (Number.isNaN(createdAt.getTime())) {
      return;
    }

    if (createdAt.getFullYear() === currentYear && createdAt.getMonth() === currentMonth) {
      points[createdAt.getDate() - 1].count += 1;
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
    maxCount,
    baseY,
  };
}

function DashboardPage({ app, navigate }) {
  const usersMonthChart = useMemo(() => buildUsersMonthChart(app.users), [app.users]);
  const totalWindows = Math.max(1, Math.ceil(usersMonthChart.points.length / CHART_WINDOW_SIZE));
  const [chartWindowIndex, setChartWindowIndex] = useState(totalWindows - 1);

  useEffect(() => {
    setChartWindowIndex(totalWindows - 1);
  }, [totalWindows]);

  const startIndex = chartWindowIndex * CHART_WINDOW_SIZE;
  const visiblePoints = usersMonthChart.points.slice(startIndex, startIndex + CHART_WINDOW_SIZE);
  const visibleTotal = visiblePoints.reduce((sum, point) => sum + point.count, 0);
  const visiblePeak = visiblePoints.reduce((highest, point) => Math.max(highest, point.count), 0);
  const visibleRangeStart = visiblePoints[0]?.day ?? 1;
  const visibleRangeEnd = visiblePoints.at(-1)?.day ?? visibleRangeStart;
  const isViewingLatestWindow = chartWindowIndex === totalWindows - 1;
  const chartGeometry = useMemo(() => buildLineChartGeometry(visiblePoints), [visiblePoints]);
  const latestPoint = chartGeometry.chartPoints.at(-1);

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
                <span className={`chart-window-label${isViewingLatestWindow ? ' is-current' : ''}`}>
                  Dias {String(visibleRangeStart).padStart(2, '0')} a {String(visibleRangeEnd).padStart(2, '0')}
                </span>
                <button
                  className="secondary-button chart-nav-button"
                  type="button"
                  onClick={() => setChartWindowIndex((current) => Math.max(0, current - 1))}
                  disabled={chartWindowIndex === 0}
                >
                  Anteriores
                </button>
                <button
                  className="secondary-button chart-nav-button"
                  type="button"
                  onClick={() => setChartWindowIndex((current) => Math.min(totalWindows - 1, current + 1))}
                  disabled={isViewingLatestWindow}
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
                ? `Pico geral no dia ${usersMonthChart.peakDay}, com ${usersMonthChart.peakCount} cadastro(s). Nesta janela, o maior volume foi ${visiblePeak}.`
                : 'Assim que surgirem novos usuarios neste mes, a linha aparecera aqui com o periodo mais recente em destaque.'}
            </p>
          </div>

          <div className="chart-focus-row">
            <span className="chart-focus-badge">{isViewingLatestWindow ? 'Foco atual' : 'Periodo anterior'}</span>
            <strong>{visibleTotal} cadastro(s) neste recorte visivel</strong>
            <span>{totalWindows > 1 ? `Janela ${chartWindowIndex + 1} de ${totalWindows}` : 'Mes atual completo'}</span>
          </div>

          <div className="chart-scroll">
            <div className="line-chart-shell">
              <svg
                className="line-chart-svg"
                viewBox={`0 0 ${chartGeometry.svgWidth} ${chartGeometry.svgHeight}`}
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

                {chartGeometry.gridLines.map((gridLine) => (
                  <line
                    key={gridLine.y}
                    className="chart-grid-line"
                    x1={CHART_PADDING.left}
                    x2={chartGeometry.svgWidth - CHART_PADDING.right}
                    y1={gridLine.y}
                    y2={gridLine.y}
                  />
                ))}

                {chartGeometry.areaPath ? <path className="chart-area" d={chartGeometry.areaPath} /> : null}
                {chartGeometry.linePath ? <path className="chart-line" d={chartGeometry.linePath} /> : null}

                {chartGeometry.chartPoints.map((point) => (
                  <g key={point.day}>
                    <circle
                      className={`chart-dot${latestPoint?.day === point.day ? ' chart-dot-active' : ''}`}
                      cx={point.x}
                      cy={point.y}
                      r={latestPoint?.day === point.day ? 6 : 4.5}
                    />
                    <text className="chart-point-value" x={point.x} y={point.y - 12} textAnchor="middle">
                      {point.count}
                    </text>
                    <text className="chart-axis-text" x={point.x} y={chartGeometry.baseY + 18} textAnchor="middle">
                      {String(point.day).padStart(2, '0')}
                    </text>
                  </g>
                ))}
              </svg>
            </div>
          </div>
        </article>

        <article className="card section-card chart-card future-chart-card">
          <SectionIntro
            eyebrow="Vendas"
            title="Espaco para o proximo grafico"
            description="Area reservada para voce adicionar depois um grafico de vendas."
            actions={
              <button className="primary-button alt-button" type="button" onClick={() => navigate('clothing')}>
                Ver roupas
              </button>
            }
          />

          <div className="future-chart-placeholder">
            <span className="future-chart-badge">Em preparacao</span>
            <strong>Grafico de vendas</strong>
            <p>Este bloco ja fica separado no layout para voce encaixar a proxima visualizacao sem mexer na estrutura.</p>
          </div>
        </article>
      </section>

      <section className="stats-grid">
        <StatCard label="Usuarios cadastrados" value={app.users.length} detail="Total salvo na base" />
        <StatCard label="Roupas cadastradas" value={app.clothing.length} detail="Itens no catalogo" tone="accent-card" />
        <StatCard label="Estoque baixo" value={app.lowStockCount} detail="Itens com 5 ou menos" tone="warm-card" />
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
            {!app.loading && app.recentUsers.length === 0 ? <p className="empty-state">Nenhum usuario cadastrado.</p> : null}
            {app.recentUsers.map((user) => (
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
            {!app.loading && app.recentClothing.length === 0 ? <p className="empty-state">Nenhuma roupa cadastrada.</p> : null}
            {app.recentClothing.map((item) => (
              <article key={item.id} className="mini-card">
                <strong>{item.name}</strong>
                <span>
                  {item.color} • Tam. {item.size} • Estoque {item.stock}
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
