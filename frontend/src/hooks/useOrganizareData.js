import { useEffect, useMemo, useState } from 'react';

const API_BASE = (import.meta.env.VITE_API_URL ?? '').replace(/\/$/, '');

const emptyUserForm = {
  name: '',
  email: '',
  cellPhone: '',
  birthMonth: '',
  birthDay: '',
};

const emptyClothingForm = {
  name: '',
  price: '',
  stock: '',
  size: '',
  color: '',
};

const initialFeedback = { type: '', message: '' };

function useOrganizareData() {
  const [users, setUsers] = useState([]);
  const [clothing, setClothing] = useState([]);
  const [userForm, setUserForm] = useState(emptyUserForm);
  const [clothingForm, setClothingForm] = useState(emptyClothingForm);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState({ user: false, clothing: false });
  const [deletingId, setDeletingId] = useState('');
  const [feedback, setFeedback] = useState(initialFeedback);
  const [userSearch, setUserSearch] = useState('');
  const [clothingSearch, setClothingSearch] = useState('');

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

  const lowStockCount = useMemo(
    () => clothing.filter((item) => Number(item.stock) <= 5).length,
    [clothing],
  );

  const recentUsers = useMemo(() => users.slice(0, 4), [users]);
  const recentClothing = useMemo(() => clothing.slice(0, 4), [clothing]);

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
    try {
      setLoading(true);
      const [usersData, clothingData] = await Promise.all([
        requestJson('/api/users'),
        requestJson('/api/clothing'),
      ]);

      setUsers(usersData ?? []);
      setClothing(clothingData ?? []);
    } catch (error) {
      setFeedback({ type: 'error', message: error.message });
    } finally {
      setLoading(false);
    }
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

  async function createUser(event) {
    event.preventDefault();

    try {
      setSubmitting((current) => ({ ...current, user: true }));
      const payload = {
        ...userForm,
        birthMonth: Number(userForm.birthMonth),
        birthDay: Number(userForm.birthDay),
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

  async function deleteUser(id) {
    try {
      setDeletingId(`user-${id}`);
      await requestJson(`/api/users/${id}`, { method: 'DELETE' });
      setUsers((current) => current.filter((user) => user.id !== id));
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
      setFeedback({ type: 'success', message: 'Roupa removida com sucesso.' });
    } catch (error) {
      setFeedback({ type: 'error', message: error.message });
    } finally {
      setDeletingId('');
    }
  }

  return {
    clothing,
    clothingForm,
    clothingSearch,
    createClothing,
    createUser,
    deleteClothing,
    deleteUser,
    deletingId,
    feedback,
    filteredClothing,
    filteredUsers,
    loadDashboard,
    loading,
    lowStockCount,
    recentClothing,
    recentUsers,
    setClothingSearch,
    setUserSearch,
    submitting,
    updateClothingField,
    updateUserField,
    userForm,
    userSearch,
    users,
    clearFeedback,
  };
}

export default useOrganizareData;
