import { Container } from "react-bootstrap";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Header from "./components/Header";
import Footer from "./components/Footer";
import UserGroupDetailScreen from "./screens/UserGroupDetailScreen";
import BrowseGroupsScreen from "./screens/BrowseGroupsScreen";
import MyGroupsScreen from "./screens/MyGroupsScreen";
import GroupDetailScreen from "./screens/GroupDetailScreen";
import GroupExpensesScreen from "./screens/GroupExpensesScreen";
import CreateGroupScreen from "./screens/CreateGroupScreen";

const App = () => {
  return (
    <Router>
      <Header />
      <main className="py-3">
        <Container>
          <Routes>
            <Route path="/" element={<BrowseGroupsScreen />} exact />
            <Route path="/group/:id/user/:userId" element={<UserGroupDetailScreen />} />
            <Route path="/mygroups" element={<MyGroupsScreen />} />
            <Route path="/group/:id/" element={<GroupDetailScreen />} />
            <Route path="/mygroups/:id/expenses" element={<GroupExpensesScreen />} />
            <Route path="/groups/create" element={<CreateGroupScreen />} />
          </Routes>
        </Container>
      </main>
      <Footer />
    </Router>
  );
};

export default App;
