import { useEffect, useMemo, useState } from 'react';

const API_BASE = (import.meta.env.VITE_API_URL ?? '').replace(/\/$/, '');

const emptyUserForm = {
  name: '',
  email: '',
  cellPhone: '',
  birthday: '',
};

const emptyClothingForm = {
  name: '',
  price: '',
  stock: '',
  size: '',
  color: '',
};

const emptyBuyForm = {
  userId: '',
  clothingId: '',
  quantity: '1',
};

const initialFeedback = { type: '', message: '' };

function getTimestamp(value) {
  const timestamp = new Date(value ?? '').getTime();
  return Number.isNaN(timestamp) ? 0 : timestamp;
}

function isInCurrentMonth(value) {
  const date = new Date(value ?? '');
  const now = new Date();
  return (
    !Number.isNaN(date.getTime()) &&
    date.getFullYear() === now.getFullYear() &&
    date.getMonth() === now.getMonth()
  );
}

function parseBirthdayInput(value) {
  const [year, month, day] = String(value ?? '')
    .split('-')
    .map((part) => Number(part));

  if (!year || !month || !day) {
    throw new Error('Selecione uma data de aniversario valida.');
  }

  return {
    birthMonth: month,
    birthDay: day,
  };
}

function useOrganizareData() {
  const [users, setUsers] = useState([]);
  const [clothing, setClothing] = useState([]);
  const [buys, setBuys] = useState([]);
  const [userForm, setUserForm] = useState(emptyUserForm);
  const [clothingForm, setClothingForm] = useState(emptyClothingForm);
  const [buyForm, setBuyForm] = useState(emptyBuyForm);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState({ user: false, clothing: false, buy: false });
  const [deletingId, setDeletingId] = useState('');
  const [updatingStockId, setUpdatingStockId] = useState('');
  const [feedback, setFeedback] = useState(initialFeedback);
  const [userSearch, setUserSearch] = useState('');
  const [clothingSearch, setClothingSearch] = useState('');
  const [buySearch, setBuySearch] = useState('');

  useEffect(() => {
    void loadDashboard();
  }, []);

  const filteredUsers = useMemo(() => {
    const term = userSearch.trim().toLowerCase();
    if (!term) {
      return users;
    }

    return users.filter((user) =>
      [user.name, user.email, user.cellPhone].some((value) =>
        String(value).toLowerCase().includes(term),
      ),
    );
  }, [userSearch, users]);

  const filteredClothing = useMemo(() => {
    const term = clothingSearch.trim().toLowerCase();
    if (!term) {
      return clothing;
    }

    return clothing.filter((item) =>
      [item.name, item.color, item.size].some((value) =>
        String(value).toLowerCase().includes(term),
      ),
    );
  }, [clothingSearch, clothing]);

  const filteredBuys = useMemo(() => {
    const term = buySearch.trim().toLowerCase();
    if (!term) {
      return buys;
    }

    return buys.filter((buy) =>
      [buy.userName, buy.clothingName, buy.orderDate, buy.totalPrice, buy.quantity].some((value) =>
        String(value).toLowerCase().includes(term),
      ),
    );
  }, [buySearch, buys]);

  const lowStockCount = useMemo(
    () => clothing.filter((item) => Number(item.stock) <= 5).length,
    [clothing],
  );

  const totalRevenue = useMemo(
    () => buys.reduce((sum, buy) => sum + Number(buy.totalPrice || 0), 0),
    [buys],
  );

  const currentMonthRevenue = useMemo(
    () =>
      buys
        .filter((buy) => isInCurrentMonth(buy.orderDate))
        .reduce((sum, buy) => sum + Number(buy.totalPrice || 0), 0),
    [buys],
  );

  const recentUsers = useMemo(
    () =>
      [...users]
        .sort((firstUser, secondUser) => getTimestamp(secondUser.createdAt) - getTimestamp(firstUser.createdAt))
        .slice(0, 4),
    [users],
  );

  const recentClothing = useMemo(
    () => [...clothing].sort((firstItem, secondItem) => secondItem.id - firstItem.id).slice(0, 4),
    [clothing],
  );

  const recentBuys = useMemo(
    () =>
      [...buys]
        .sort((firstBuy, secondBuy) => getTimestamp(secondBuy.orderDate) - getTimestamp(firstBuy.orderDate))
        .slice(0, 4),
    [buys],
  );

  async function requestJson(path, options = {}) {
    const response = await fetch(`${API_BASE}${path}`, {
      headers: {
        'Content-Type': 'application/json',
        ...(options.headers ?? {}),
      },
      ...options,
    });

    if (response.status === 204) {
      return null;
    }

    const text = await response.text();
    const data = text ? JSON.parse(text) : null;

    if (!response.ok) {
      const message = data?.error || 'Nao foi possivel concluir a requisicao.';
      throw new Error(message);
    }

    return data;
  }

  async function loadDashboard() {
    setLoading(true);

    const [usersResult, clothingResult, buysResult] = await Promise.allSettled([
      requestJson('/api/users'),
      requestJson('/api/clothing'),
      requestJson('/api/buy'),
    ]);

    if (usersResult.status === 'fulfilled') {
      setUsers(usersResult.value ?? []);
    }

    if (clothingResult.status === 'fulfilled') {
      setClothing(clothingResult.value ?? []);
    }

    if (buysResult.status === 'fulfilled') {
      setBuys(buysResult.value ?? []);
    } else {
      setBuys([]);
    }

    const firstError =
      (usersResult.status === 'rejected' && usersResult.reason) ||
      (clothingResult.status === 'rejected' && clothingResult.reason) ||
      (buysResult.status === 'rejected' && buysResult.reason);

    if (firstError) {
      setFeedback({ type: 'error', message: firstError.message });
    }

    setLoading(false);
  }

  function clearFeedback() {
    setFeedback(initialFeedback);
  }

  function updateUserField(event) {
    const { name, value } = event.target;
    setUserForm((current) => ({ ...current, [name]: value }));
  }

  function updateClothingField(event) {
    const { name, value } = event.target;
    setClothingForm((current) => ({ ...current, [name]: value }));
  }

  function updateBuyField(event) {
    const { name, value } = event.target;
    setBuyForm((current) => ({ ...current, [name]: value }));
  }

  async function createUser(event) {
    event.preventDefault();

    try {
      setSubmitting((current) => ({ ...current, user: true }));
      const { birthMonth, birthDay } = parseBirthdayInput(userForm.birthday);
      const payload = {
        name: userForm.name,
        email: userForm.email,
        cellPhone: userForm.cellPhone,
        birthday: userForm.birthday,
        birthMonth,
        birthDay,
      };

      const createdUser = await requestJson('/api/users', {
        method: 'POST',
        body: JSON.stringify(payload),
      });

      setUsers((current) => [createdUser, ...current]);
      setUserForm(emptyUserForm);
      setFeedback({ type: 'success', message: 'Usuario cadastrado com sucesso.' });
    } catch (error) {
      setFeedback({ type: 'error', message: error.message });
    } finally {
      setSubmitting((current) => ({ ...current, user: false }));
    }
  }

  async function createClothing(event) {
    event.preventDefault();

    try {
      setSubmitting((current) => ({ ...current, clothing: true }));
      const payload = {
        ...clothingForm,
        price: Number(clothingForm.price),
        stock: Number(clothingForm.stock),
      };

      const createdClothing = await requestJson('/api/clothing', {
        method: 'POST',
        body: JSON.stringify(payload),
      });

      setClothing((current) => [createdClothing, ...current]);
      setClothingForm(emptyClothingForm);
      setFeedback({ type: 'success', message: 'Roupa cadastrada com sucesso.' });
    } catch (error) {
      setFeedback({ type: 'error', message: error.message });
    } finally {
      setSubmitting((current) => ({ ...current, clothing: false }));
    }
  }

  async function createBuy(event) {
    event.preventDefault();

    const userId = Number(buyForm.userId);
    const clothingId = Number(buyForm.clothingId);
    const quantity = Number(buyForm.quantity);
    const selectedClothing = clothing.find((item) => item.id === clothingId);

    if (!userId || !clothingId || !quantity || quantity < 1) {
      setFeedback({ type: 'error', message: 'Selecione usuario, roupa e quantidade validos.' });
      return;
    }

    if (!selectedClothing) {
      setFeedback({ type: 'error', message: 'Selecione uma roupa existente para registrar a compra.' });
      return;
    }

    try {
      setSubmitting((current) => ({ ...current, buy: true }));
      const createdBuy = await requestJson('/api/buy', {
        method: 'POST',
        body: JSON.stringify({
          userId,
          clothingId,
          quantity,
        }),
      });

      setBuys((current) => [createdBuy, ...current]);
      setClothing((current) =>
        current.map((item) =>
          item.id === clothingId
            ? { ...item, stock: Math.max(0, Number(item.stock) - quantity) }
            : item,
        ),
      );
      setBuyForm(emptyBuyForm);
      setFeedback({ type: 'success', message: 'Compra registrada com sucesso.' });
    } catch (error) {
      setFeedback({ type: 'error', message: error.message });
    } finally {
      setSubmitting((current) => ({ ...current, buy: false }));
    }
  }

  async function deleteUser(id) {
    try {
      setDeletingId(`user-${id}`);
      await requestJson(`/api/users/${id}`, { method: 'DELETE' });
      setUsers((current) => current.filter((user) => user.id !== id));
      setBuys((current) => current.filter((buy) => buy.userId !== id));
      setFeedback({ type: 'success', message: 'Usuario removido com sucesso.' });
    } catch (error) {
      setFeedback({ type: 'error', message: error.message });
    } finally {
      setDeletingId('');
    }
  }

  async function deleteClothing(id) {
    try {
      setDeletingId(`clothing-${id}`);
      await requestJson(`/api/clothing/${id}`, { method: 'DELETE' });
      setClothing((current) => current.filter((item) => item.id !== id));
      setBuys((current) => current.filter((buy) => buy.clothingId !== id));
      setFeedback({ type: 'success', message: 'Roupa removida com sucesso.' });
    } catch (error) {
      setFeedback({ type: 'error', message: error.message });
    } finally {
      setDeletingId('');
    }
  }

  async function deleteBuy(id) {
    const removedBuy = buys.find((buy) => buy.id === id);

    try {
      setDeletingId(`buy-${id}`);
      await requestJson(`/api/buy/${id}`, { method: 'DELETE' });
      setBuys((current) => current.filter((buy) => buy.id !== id));

      if (removedBuy) {
        setClothing((current) =>
          current.map((item) =>
            item.id === removedBuy.clothingId
              ? { ...item, stock: Number(item.stock) + Number(removedBuy.quantity) }
              : item,
          ),
        );
      }

      setFeedback({ type: 'success', message: 'Compra removida com sucesso.' });
    } catch (error) {
      setFeedback({ type: 'error', message: error.message });
    } finally {
      setDeletingId('');
    }
  }

  async function updateClothingStock(item, nextStock) {
    const newStock = Math.max(0, Number(nextStock));

    try {
      setUpdatingStockId(`clothing-${item.id}`);
      const payload = {
        name: item.name,
        price: Number(item.price),
        stock: Number(newStock),
        size: item.size,
        color: item.color,
      };

      const updatedClothing = await requestJson(`/api/clothing/${item.id}`, {
        method: 'PUT',
        body: JSON.stringify(payload),
      });

      setClothing((current) =>
        current.map((currentItem) => (currentItem.id === item.id ? updatedClothing : currentItem)),
      );
      setFeedback({ type: 'success', message: 'Estoque alterado com sucesso.' });
    } catch (error) {
      setFeedback({ type: 'error', message: error.message });
    } finally {
      setUpdatingStockId('');
    }
  }

  return {
    buys,
    buyForm,
    buySearch,
    clothing,
    clothingForm,
    clothingSearch,
    createBuy,
    createClothing,
    createUser,
    currentMonthRevenue,
    updateClothingStock,
    deleteBuy,
    deleteClothing,
    deleteUser,
    deletingId,
    feedback,
    filteredBuys,
    filteredClothing,
    filteredUsers,
    loadDashboard,
    loading,
    lowStockCount,
    recentBuys,
    recentClothing,
    recentUsers,
    setBuySearch,
    setClothingSearch,
    setUserSearch,
    submitting,
    totalRevenue,
    updatingStockId,
    updateBuyField,
    updateClothingField,
    updateUserField,
    userForm,
    userSearch,
    users,
    clearFeedback,
  };
}

export default useOrganizareData;
